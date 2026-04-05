package com.in.cafe.com.in.cafe.ServiceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.in.cafe.com.in.cafe.JWT.JwtFilter;
import com.in.cafe.com.in.cafe.POJO.Bill;
import com.in.cafe.com.in.cafe.Service.BillService;
import com.in.cafe.com.in.cafe.constents.CafeConstants;
import com.in.cafe.com.in.cafe.dao.BillDao;
import com.in.cafe.com.in.cafe.utils.CafeUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger log = LoggerFactory.getLogger(BillServiceImpl.class);

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private BillDao billDao;

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try {
            List<Bill> list;

            if (jwtFilter.isAdmin()) {
                list = billDao.getAllBills();
            } else {
                String currentUser = jwtFilter.getCurrentUser();

                if (currentUser == null) {
                    return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
                }

                list = billDao.getBillByUserName(currentUser);
            }

            return new ResponseEntity<>(list, HttpStatus.OK);

        } catch (Exception ex) {
            log.error("Error in getBills", ex);
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try {
            byte[] byteArray;

            if (!requestMap.containsKey("uuid")) {
                requestMap.put("uuid", UUID.randomUUID().toString());
            }

            String uuid = String.valueOf(requestMap.get("uuid"));
            String filePath = CafeConstants.STORE_LOCATION + File.separator + uuid + ".pdf";

            if (CafeUtils.isFileExist(filePath)) {
                byteArray = getByteArray(filePath);
            } else {
                boolean isGenerated = generatePdfFile(filePath, requestMap);

                if (!isGenerated) {
                    return new ResponseEntity<>(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);
                }

                byteArray = getByteArray(filePath);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                    .filename(uuid + ".pdf")
                    .build()
            );

            return new ResponseEntity<>(byteArray, headers, HttpStatus.OK);

        } catch (Exception ex) {
            log.error("Error in getPdf", ex);
            return new ResponseEntity<>(new byte[0], HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private byte[] getByteArray(String filePath) throws Exception {
        File file = new File(filePath);

        try (FileInputStream fis = new FileInputStream(file)) {
            return IOUtils.toByteArray(fis);
        }
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            Optional<Bill> optional = billDao.findById(id);

            if (optional.isPresent()) {
                billDao.deleteById(id);
                return new ResponseEntity<>("Bill Deleted Successfully", HttpStatus.OK);
            }

            return new ResponseEntity<>("Bill id does not exist", HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.error("Error in deleteBill", ex);
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try {
            if (!validateRequestMap(requestMap)) {
                return new ResponseEntity<>("Invalid Data", HttpStatus.BAD_REQUEST);
            }

            if (!requestMap.containsKey("uuid")) {
                requestMap.put("uuid", UUID.randomUUID().toString());
            }

            String uuid = String.valueOf(requestMap.get("uuid"));
            String filePath = CafeConstants.STORE_LOCATION + File.separator + uuid + ".pdf";

            boolean isGenerated = generatePdfFile(filePath, requestMap);

            if (isGenerated) {
                Bill bill = new Bill();

                bill.setUuid(uuid);
                bill.setName(String.valueOf(requestMap.get("name")));
                bill.setEmail(String.valueOf(requestMap.get("email")));
                bill.setContactNumber(String.valueOf(requestMap.get("contactNumber")));
                bill.setPaymentMethod(String.valueOf(requestMap.get("paymentMethod")));
                bill.setProductDetails(String.valueOf(requestMap.get("productDetails")));
                bill.setCreatedBy(jwtFilter.getCurrentUser());

                // ✅ Fix: Ensure total is never null
                if (requestMap.get("totalAmount") != null) {
                    bill.setTotal(Integer.parseInt(String.valueOf(requestMap.get("totalAmount"))));
                } else {
                    bill.setTotal(0); // default to 0 if missing
                }

                billDao.save(bill);

                return new ResponseEntity<>("Report generated successfully", HttpStatus.OK);
            }

            return new ResponseEntity<>("Report generation failed", HttpStatus.INTERNAL_SERVER_ERROR);

        } catch (Exception ex) {
            log.error("Error generating report", ex);
            return new ResponseEntity<>("Error generating report", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean generatePdfFile(String filePath, Map<String, Object> requestMap) {
        Document document = new Document(PageSize.A4);

        try {
            File file = new File(filePath);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            document.open();

            PdfPTable outerTable = new PdfPTable(1);
            outerTable.setWidthPercentage(100);

            PdfPCell outerCell = new PdfPCell();
            outerCell.setPadding(10);
            outerCell.setBorderWidth(2);

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);

            Paragraph title = new Paragraph("Cafe Management System", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);

            outerCell.addElement(title);
            outerCell.addElement(new Paragraph(" "));

            outerCell.addElement(new Paragraph("Name: " + requestMap.get("name")));
            outerCell.addElement(new Paragraph("Contact Number: " + requestMap.get("contactNumber")));
            outerCell.addElement(new Paragraph("Email: " + requestMap.get("email")));
            outerCell.addElement(new Paragraph("Payment Method: " + requestMap.get("paymentMethod")));
            outerCell.addElement(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            table.addCell(getYellowHeader("Name"));
            table.addCell(getYellowHeader("Category"));
            table.addCell(getYellowHeader("Quantity"));
            table.addCell(getYellowHeader("Price"));
            table.addCell(getYellowHeader("Sub Total"));

            String productDetails = String.valueOf(requestMap.get("productDetails"));
            JSONArray jsonArray = CafeUtils.getJsonArrayFromString(productDetails);

            int total = 0;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String name = jsonObject.optString("name");
                String category = jsonObject.optString("category");
                int quantity = jsonObject.optInt("quantity");
                int price = jsonObject.optInt("price");

                int subTotal = quantity * price;
                total += subTotal;

                table.addCell(getNormalCell(name));
                table.addCell(getNormalCell(category));
                table.addCell(getNormalCell(String.valueOf(quantity)));
                table.addCell(getNormalCell(String.valueOf(price)));
                table.addCell(getNormalCell(String.valueOf(subTotal)));
            }

            outerCell.addElement(table);
            outerCell.addElement(new Paragraph(" "));

            if (requestMap.get("totalAmount") != null) {
                outerCell.addElement(
                    new Paragraph("Total Amount: " + requestMap.get("totalAmount"))
                );
            } else {
                outerCell.addElement(new Paragraph("Total Amount: " + total));
            }

            outerCell.addElement(new Paragraph(" "));
            outerCell.addElement(new Paragraph("Thank you for visiting. Please visit again!"));

            outerTable.addCell(outerCell);
            document.add(outerTable);

            return true;

        } catch (Exception ex) {
            log.error("Error generating PDF", ex);
            return false;
        } finally {
            document.close();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name")
                && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email")
                && requestMap.containsKey("paymentMethod")
                && requestMap.containsKey("productDetails");
    }

    private PdfPCell getYellowHeader(String text) {
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.YELLOW);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);

        return cell;
    }

    private PdfPCell getNormalCell(String text) {
        Font font = new Font(Font.FontFamily.HELVETICA, 11);

        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        return cell;
    }
}