package com.nju.edu.erp.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.nju.edu.erp.dao.*;
import com.nju.edu.erp.enums.sheetState.*;
import com.nju.edu.erp.exception.MyServiceException;
import com.nju.edu.erp.model.po.AccountPO;
import com.nju.edu.erp.model.po.CustomerPO;
import com.nju.edu.erp.model.po.ProductPO;
import com.nju.edu.erp.model.po.business.BookPO;
import com.nju.edu.erp.model.po.salary.SalarySheetPO;
import com.nju.edu.erp.model.vo.*;
import com.nju.edu.erp.model.vo.business.*;
import com.nju.edu.erp.model.vo.payment.PaymentSheetVO;
import com.nju.edu.erp.model.vo.purchase.PurchaseSheetVO;
import com.nju.edu.erp.model.vo.purchaseReturns.PurchaseReturnsSheetVO;
import com.nju.edu.erp.model.vo.receipt.ReceiptSheetVO;
import com.nju.edu.erp.model.vo.sale.SaleSheetVO;
import com.nju.edu.erp.model.vo.saleReturns.SaleReturnsSheetVO;
import com.nju.edu.erp.service.*;
import com.nju.edu.erp.utils.IdGenerator;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final BusinessDao businessDao;

    private final PurchaseService purchaseService;

    private final PurchaseReturnsService purchaseReturnsService;

    private final SaleService saleService;

    private final SaleReturnsService saleReturnsService;

    private final PaymentService paymentService;

    private final ReceiptService receiptService;

    private final SalaryService salaryService;

    private final PurchaseSheetDao purchaseSheetDao;

    private final PurchaseReturnsSheetDao purchaseReturnsSheetDao;

    private final SaleSheetDao saleSheetDao;

    private final SaleReturnsSheetDao saleReturnsSheetDao;

    private final PaymentDao paymentDao;

    private final ReceiptDao receiptDao;

    private final SalaryDao salaryDao;

    private final AccountDao accountDao;

    private final CustomerDao customerDao;

    private final ProductDao productDao;

    @Autowired
    public BusinessServiceImpl(BusinessDao businessDao, PurchaseService purchaseService, PurchaseReturnsService purchaseReturnsService, SaleService saleService, SaleReturnsService saleReturnsService, PaymentService paymentService, ReceiptService receiptService, SalaryService salaryService, PurchaseSheetDao purchaseSheetDao, PurchaseReturnsSheetDao purchaseReturnsSheetDao, SaleSheetDao saleSheetDao, SaleReturnsSheetDao saleReturnsSheetDao, PaymentDao paymentDao, ReceiptDao receiptDao, SalaryDao salaryDao, AccountDao accountDao, CustomerDao customerDao, ProductDao productDao) {
        this.businessDao = businessDao;
        this.purchaseService = purchaseService;
        this.purchaseReturnsService = purchaseReturnsService;
        this.saleService = saleService;
        this.saleReturnsService = saleReturnsService;
        this.paymentService = paymentService;
        this.receiptService = receiptService;
        this.salaryService = salaryService;

        this.purchaseSheetDao = purchaseSheetDao;
        this.purchaseReturnsSheetDao = purchaseReturnsSheetDao;
        this.saleSheetDao = saleSheetDao;
        this.saleReturnsSheetDao = saleReturnsSheetDao;
        this.paymentDao = paymentDao;
        this.receiptDao = receiptDao;
        this.salaryDao = salaryDao;
        this.accountDao = accountDao;
        this.customerDao = customerDao;
        this.productDao = productDao;
    }

    @Override
    public List<SaleDetailVO> showSaleDetails(String beginDateStr, String endDateStr) {
        if (beginDateStr == null && endDateStr == null){ //????????????????????????????????????
            List<SaleDetailVO> saleDetailVOList = businessDao.getAllSaleDetails();
            return saleDetailVOList;
        }
        else if (beginDateStr == null || endDateStr == null) //???????????????????????????
            return null;
        else { //?????????????????????????????????????????????
            try {
                DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date beginDate = simpleDateFormat.parse(beginDateStr);
                Date endDate = simpleDateFormat.parse(endDateStr);

                List<SaleDetailVO> saleDetailVOList = businessDao.getSaleDetails(beginDate, endDate);
                return saleDetailVOList;
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }


//    @Override ?????????
//    public void exportSaleDetails(HttpServletResponse response){
//        List<SaleDetailVO> all = showSaleDetails(null,null);
//
//        List<SaleDetailExcel> excels = new ArrayList<>();
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        ExcelWriter excelWriter = EasyExcel.write(outputStream).build();
//        WriteSheet saleDetail = EasyExcel.writerSheet(0, "????????????").head(SaleDetailExcel.class).build();
//        excelWriter.write(all, saleDetail);
//        excelWriter.finish();
//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String format = simpleDateFormat.format(new Date());
//        String fileName = format + "???????????????.xlsx";
//
//        try {
//            outputStream.writeTo(response.getOutputStream());
//            response.setContentType("application/octet-stream");
//            fileName = URLEncoder.encode(fileName, "UTF-8");
//            response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"; filename*=utf-8''%s", fileName, fileName));
//            response.setContentLengthLong(outputStream.size());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public List<MyFilter> showBusinessProcess(String beginDateStr, String endDateStr){
        List<MyFilter> myFilterList = getSheet();
        if (beginDateStr != null && endDateStr != null){ //????????????????????????
            try {
                DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date beginDate = simpleDateFormat.parse(beginDateStr);
                Date endDate = simpleDateFormat.parse(endDateStr);
                //???????????????????????????????????????
                myFilterList.removeIf(myFilter -> beginDate.compareTo(((SheetVO) myFilter.getValue()).getCreateTime()) > 0 || endDate.compareTo(((SheetVO) myFilter.getValue()).getCreateTime()) < 0);
            }catch (ParseException e){
                e.printStackTrace();
                return null;
            }
        }
        else if (beginDateStr != null || endDateStr != null) //???????????????????????????????????????????????????
            return null;
        //??????????????????????????????????????????
        myFilterList.sort(new Comparator<MyFilter>() {
            @Override
            public int compare(MyFilter o1, MyFilter o2) {
                return ((SheetVO) o2.getValue()).getCreateTime().compareTo(((SheetVO) o1.getValue()).getCreateTime());
            }
        });
        return myFilterList;
    }

    @Override
    public void writeBack(UserVO userVO, SheetVO sheetVO) {
        String type = sheetVO.getId().split("-")[0]; //?????????????????????????????????????????????????????????
        //???????????????????????????????????????????????????????????????????????????
        switch (type){
            case "JHD":
                writeBackPurchaseSheet(userVO, (PurchaseSheetVO) sheetVO);
                break;
            case "JHTHD":
                writeBackPurchaseReturnsSheet(userVO, (PurchaseReturnsSheetVO) sheetVO);
                break;
            case "XSD":
                writeBackSaleSheet(userVO, (SaleSheetVO) sheetVO);
                break;
            case "XSTHD":
                writeBackSaleReturnsSheet(userVO, (SaleReturnsSheetVO) sheetVO);
                break;
            case "FKD":
                writeBackPaymentSheet(userVO, (PaymentSheetVO) sheetVO);
                break;
            case "SKD":
                writeBackReceiptSheet(userVO, (ReceiptSheetVO) sheetVO);
                break;
            case "GZD":
                //??????????????????????????????
                //writeBackSalarySheet(userVO,(SalarySheetPO) sheetVO);
                break;
            default:
                throw new MyServiceException("P0001","????????????????????????????????????");
        }
    }

    @Override
    public BusinessSituationTable showBusinessSituation(String beginDateStr, String endDateStr) {
        BusinessSituationTable businessSituationTable = new BusinessSituationTable();
        List<BusinessSituationItem> businessSituationItemList = new ArrayList<>();

        if (beginDateStr != null && endDateStr != null) { //????????????????????????
            try {
                DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date beginDate = simpleDateFormat.parse(beginDateStr);
                Date endDate = simpleDateFormat.parse(endDateStr);
                if (beginDate.compareTo(endDate)>0) //???????????????????????????????????????null
                    return null;
                businessSituationItemList = businessDao.getBusinessSituationItem(beginDate, endDate);

                businessSituationTable.setBeginDate(beginDate);
                businessSituationTable.setEndDate(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
        else if (beginDateStr != null || endDateStr != null) //????????????????????????????????????????????????????????????null
            return null;
        else { //????????????????????????????????????
            businessSituationItemList = businessDao.getAllBusinessSituationItem();

            businessSituationTable.setBeginDate(null);
            businessSituationTable.setEndDate(null);
        }

        //???????????????????????????
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalPay = BigDecimal.ZERO;
        businessSituationTable.setItems(businessSituationItemList);
        for (BusinessSituationItem item:businessSituationItemList) {
            if (item.getType().equals("??????"))
                totalIncome = totalIncome.add(item.getAmount());
            if (item.getType().equals("??????"))
                totalPay = totalPay.add(item.getAmount());
        }
        //????????????
        BigDecimal profit = totalIncome.subtract(totalPay);

        businessSituationTable.setProfit(profit);
        businessSituationTable.setTotalIncome(totalIncome);
        businessSituationTable.setTotalPay(totalPay);

        return businessSituationTable;
    }

    @Override
    public void createBook() {
        BookPO bookPO = new BookPO();

        BookPO latest = businessDao.getLatestBook();
        String id = IdGenerator.generateSheetId(latest==null?null:latest.getId(),"TZ");
        bookPO.setId(id);
        bookPO.setCreateTime(new Date());

        List<AccountPO> accountPOList = accountDao.findAll();
        List<CustomerPO> customerPOList = customerDao.findAll();
        List<ProductPO> productPOList = productDao.findAll();

        //?????????????????????
        //?????????????????????????????????????????????????????????????????????
        businessDao.saveBook(bookPO);
        businessDao.saveAccountBook(accountPOList, id);
        businessDao.saveCustomerBook(customerPOList, id);
        businessDao.saveProductBook(productPOList, id);
    }

    @Override
    public BookVO getBookById(String bookId){
        BookVO bookVO = new BookVO();

        BookPO bookPO = businessDao.getBookById(bookId);
        if (bookPO==null)
            throw new MyServiceException("P0002","??????????????????????????????");
        List<AccountPO> accountPOList = businessDao.getAccountBookById(bookId);
        List<CustomerPO> customerPOList = businessDao.getCustomerBookById(bookId);
        List<ProductPO> productPOList = businessDao.getProductBookById(bookId);

        List<AccountVO> accountVOList = new ArrayList<>();
        List<CustomerVO> customerVOList = new ArrayList<>();
        List<ProductInfoVO> productVOList = new ArrayList<>();

        for (AccountPO accountPO:accountPOList){
            AccountVO accountVO = new AccountVO();
            BeanUtils.copyProperties(accountPO,accountVO);
            accountVOList.add(accountVO);
        }
        for (CustomerPO customerPO:customerPOList){
            CustomerVO customerVO = new CustomerVO();
            BeanUtils.copyProperties(customerPO,customerVO);
            customerVOList.add(customerVO);
        }
        for (ProductPO productPO:productPOList){
            ProductInfoVO productVO = new ProductInfoVO();
            BeanUtils.copyProperties(productPO,productVO);
            productVO.setRecentPp(null);
            productVO.setRecentRp(null);
            productVOList.add(productVO);
        }

        bookVO.setId(bookPO.getId());
        bookVO.setCreateTime(bookPO.getCreateTime());
        bookVO.setAccountList(accountVOList);
        bookVO.setCustomerList(customerVOList);
        bookVO.setProductList(productVOList);

        return bookVO;
    }

    @Override
    public List<String> getAllBookId() {
        return businessDao.getAllBookId();
    }




    private List<MyFilter> getSheet(){
        List<MyFilter> myFilterList = new ArrayList<>();

        //?????????
        List<PurchaseSheetVO> purchaseSheetVOList = purchaseService.getPurchaseSheetByState(PurchaseSheetState.SUCCESS);
        for (PurchaseSheetVO purchaseSheetVO: purchaseSheetVOList)
            myFilterList.add(new MyFilter("?????????", purchaseSheetVO));

        //???????????????
        List<PurchaseReturnsSheetVO> purchaseReturnsSheetVOList = purchaseReturnsService.getPurchaseReturnsSheetByState(PurchaseReturnsSheetState.SUCCESS);
        for (PurchaseReturnsSheetVO purchaseReturnsSheetVO: purchaseReturnsSheetVOList)
            myFilterList.add(new MyFilter("???????????????",purchaseReturnsSheetVO));

        //?????????
        List<SaleSheetVO> saleSheetVOList = saleService.getSaleSheetByState(SaleSheetState.SUCCESS);
        for (SaleSheetVO saleSheetVO:saleSheetVOList)
            myFilterList.add(new MyFilter("?????????",saleSheetVO));

        //???????????????
        List<SaleReturnsSheetVO> saleReturnsSheetVOList = saleReturnsService.getSaleReturnsSheetByState(SaleReturnsSheetState.SUCCESS);
        for (SaleReturnsSheetVO saleReturnsSheetVO:saleReturnsSheetVOList)
            myFilterList.add(new MyFilter("???????????????",saleReturnsSheetVO));

        //?????????
        List<PaymentSheetVO> paymentSheetVOList = paymentService.getSheetByState(PaymentSheetState.SUCCESS);
        for (PaymentSheetVO paymentSheetVO: paymentSheetVOList)
            myFilterList.add(new MyFilter("?????????",paymentSheetVO));

        //?????????
        List<ReceiptSheetVO> receiptSheetList = receiptService.getSheetByState(ReceiptSheetState.SUCCESS);
        for (ReceiptSheetVO receiptSheetVO: receiptSheetList)
            myFilterList.add(new MyFilter("?????????",receiptSheetVO));

        //?????????
        List<SalarySheetPO> salarySheetPOList = salaryService.getSheetByState(SalarySheetState.SUCCESS);
        for (SalarySheetPO salarySheetPO:salarySheetPOList)
            myFilterList.add(new MyFilter("?????????",salarySheetPO));

        return myFilterList;
    }

    private void writeBackPurchaseSheet(UserVO userVO, PurchaseSheetVO purchaseSheetVO){
        purchaseService.makePurchaseSheet(userVO,purchaseSheetVO);
        String sheetId = purchaseSheetDao.getLatest().getId();
        purchaseService.approval(sheetId, PurchaseSheetState.PENDING_LEVEL_2);
        purchaseService.approval(sheetId, PurchaseSheetState.SUCCESS);
    }

    private void writeBackPurchaseReturnsSheet(UserVO userVO, PurchaseReturnsSheetVO purchaseReturnsSheetVO){
        purchaseReturnsService.makePurchaseReturnsSheet(userVO, purchaseReturnsSheetVO);
        String sheetId = purchaseReturnsSheetDao.getLatest().getId();
        purchaseReturnsService.approval(sheetId, PurchaseReturnsSheetState.PENDING_LEVEL_2);
        purchaseReturnsService.approval(sheetId, PurchaseReturnsSheetState.SUCCESS);
    }

    private void writeBackSaleSheet(UserVO userVO, SaleSheetVO saleSheetVO){
        saleService.makeSaleSheet(userVO, saleSheetVO);
        String sheetId = saleSheetDao.getLatest().getId();
        saleService.approval(sheetId, SaleSheetState.PENDING_LEVEL_2);
        saleService.approval(sheetId, SaleSheetState.SUCCESS);
    }

    private void writeBackSaleReturnsSheet(UserVO userVO, SaleReturnsSheetVO saleReturnsSheetVO){
        saleReturnsService.makeSaleReturnsSheet(userVO, saleReturnsSheetVO);
        String sheetId = saleReturnsSheetDao.getLatest().getId();
        saleReturnsService.approval(sheetId, SaleReturnsSheetState.PENDING_LEVEL_2);
        saleReturnsService.approval(sheetId, SaleReturnsSheetState.SUCCESS);
    }

    private void writeBackPaymentSheet(UserVO userVO, PaymentSheetVO paymentSheetVO){
        paymentService.makeSheet(userVO, paymentSheetVO);
        String sheetId = paymentDao.getLatest().getId();
            paymentService.approval(sheetId, PaymentSheetState.SUCCESS);
    }

    private void writeBackReceiptSheet(UserVO userVO, ReceiptSheetVO receiptSheetVO){
        receiptService.makeSheet(userVO, receiptSheetVO);
        String sheetId = receiptDao.getLatest().getId();
        receiptService.approval(sheetId, ReceiptSheetState.SUCCESS);
    }

//    private void writeBackSalarySheet(UserVO userVO, SalarySheetPO salarySheetPO){
//        salaryService.makeSheet();
//        String sheetId = salaryDao.getLatest().getId();
//        salaryService.approval(sheetId, SalarySheetState.SUCCESS);
//    }

    @Override
    public Set<MyFilter> getProductSet(String beginDateStr, String endDateStr) {
        List<SaleDetailVO> saleDetailVOList = showSaleDetails(beginDateStr, endDateStr);
        Set<MyFilter> productSet = new HashSet<>();
        for (SaleDetailVO saleDetailVO:saleDetailVOList)
            productSet.add(new MyFilter(saleDetailVO.getProductName(),saleDetailVO.getProductName()));
        return productSet;
    }

    @Override
    public Set<MyFilter> getSupplierSet(String beginDateStr, String endDateStr) {
        List<SaleDetailVO> saleDetailVOList = showSaleDetails(beginDateStr, endDateStr);
        Set<MyFilter> supplierSet = new HashSet<>();
        for (SaleDetailVO saleDetailVO:saleDetailVOList)
            supplierSet.add(new MyFilter(saleDetailVO.getSupplier(), saleDetailVO.getSupplier()));
        return supplierSet;
    }

    @Override
    public Set<MyFilter> getSalesmanSet(String beginDateStr, String endDateStr) {
        List<SaleDetailVO> saleDetailVOList = showSaleDetails(beginDateStr, endDateStr);
        Set<MyFilter> salesmanSet = new HashSet<>();
        for (SaleDetailVO saleDetailVO:saleDetailVOList)
            salesmanSet.add(new MyFilter(saleDetailVO.getSalesman(), saleDetailVO.getSalesman()));
        return salesmanSet;
    }
}
