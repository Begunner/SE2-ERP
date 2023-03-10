package com.nju.edu.erp.service.Impl;

import com.nju.edu.erp.dao.CustomerDao;
import com.nju.edu.erp.dao.ProductDao;
import com.nju.edu.erp.dao.SaleSheetDao;
import com.nju.edu.erp.enums.sheetState.SaleSheetState;
import com.nju.edu.erp.model.po.*;
import com.nju.edu.erp.model.po.sale.*;
import com.nju.edu.erp.model.vo.ProductInfoVO;
import com.nju.edu.erp.model.vo.promotion.PromotionOfferInfo;
import com.nju.edu.erp.model.vo.promotion.PromotionVO;
import com.nju.edu.erp.model.vo.sale.*;
import com.nju.edu.erp.model.vo.UserVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormContentVO;
import com.nju.edu.erp.model.vo.warehouse.WarehouseOutputFormVO;
import com.nju.edu.erp.service.*;
import com.nju.edu.erp.strategy.promotion.offer.PromotionOfferInfoGenerator;
import com.nju.edu.erp.strategy.promotion.offer.PromotionOfferStrategy;
import com.nju.edu.erp.strategy.promotion.offer.PromotionOfferStrategyContext;
import com.nju.edu.erp.strategy.promotion.require.PromotionRequireStrategy;
import com.nju.edu.erp.utils.IdGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SaleServiceImpl implements SaleService {

    private final SaleSheetDao saleSheetDao;

    private final ProductDao productDao;

    private final CustomerDao customerDao;

    private final ProductService productService;

    private final CustomerService customerService;

    private final WarehouseService warehouseService;

    private final PromotionService promotionService;

    private final PromotionRequireStrategy promotionRequireStrategy;

    private final PromotionOfferStrategyContext promotionOfferStrategyContext;

    @Autowired
    public SaleServiceImpl(SaleSheetDao saleSheetDao, ProductDao productDao, CustomerDao customerDao, ProductService productService, CustomerService customerService, WarehouseService warehouseService, PromotionService promotionService, PromotionRequireStrategy promotionRequireStrategy, PromotionOfferStrategyContext promotionOfferStrategyContext) {
        this.saleSheetDao = saleSheetDao;
        this.productDao = productDao;
        this.customerDao = customerDao;
        this.productService = productService;
        this.customerService = customerService;
        this.warehouseService = warehouseService;
        this.promotionService = promotionService;
        this.promotionRequireStrategy = promotionRequireStrategy;
        this.promotionOfferStrategyContext = promotionOfferStrategyContext;
    }


    /**
     * ???????????????
     *
     * @param userVO ?????????
     * @param saleSheetVO ?????????
     */
    @Override
    @Transactional
    public void makeSaleSheet(UserVO userVO, SaleSheetVO saleSheetVO) {
        // TODO
        ////  DONE
        // ???????????????????????????SaleSheet???????????????content???SaleSheetContent??????????????????????????????????????????????????????????????????
        // ?????????service???dao???????????????????????????????????????????????????????????????
        SaleSheetPO saleSheetPO = new SaleSheetPO();
        BeanUtils.copyProperties(saleSheetVO,saleSheetPO);
        // ?????????????????????????????????????????????
        saleSheetPO.setOperator(userVO.getName());
        saleSheetPO.setCreateTime(new Date());
        // ???????????????????????????????????????????????????????????????
        SaleSheetPO latest = saleSheetDao.getLatest();
        String id = IdGenerator.generateSheetId(latest == null ? null : latest.getId(), "XSD");
        saleSheetPO.setId(id);
        saleSheetPO.setState(SaleSheetState.PENDING_LEVEL_1);
        // ???????????????saleSheetContent
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<SaleSheetContentPO> saleSheetContentPOList = new ArrayList<>();
        for (SaleSheetContentVO content: saleSheetVO.getSaleSheetContent()){
            SaleSheetContentPO saleSheetContentPO = new SaleSheetContentPO();
            BeanUtils.copyProperties(content,saleSheetContentPO);
            saleSheetContentPO.setSaleSheetId(id);
            BigDecimal unitPrice = saleSheetContentPO.getUnitPrice();
            //????????????????????????????????????????????????????????????????????????
            if(unitPrice == null) {
                ProductPO product = productDao.findById(content.getPid());
                unitPrice = product.getRetailPrice();
                saleSheetContentPO.setUnitPrice(unitPrice);
            }
            saleSheetContentPO.setTotalPrice(unitPrice.multiply(BigDecimal.valueOf(saleSheetContentPO.getQuantity())));
            saleSheetContentPOList.add(saleSheetContentPO);
            totalAmount = totalAmount.add(saleSheetContentPO.getTotalPrice());
        }
        saleSheetDao.saveBatchSheetContent(saleSheetContentPOList);
        saleSheetPO.setRawTotalAmount(totalAmount);

        if (saleSheetVO.getDiscount() == null) {
            //???????????????????????????????????????
            saleSheetVO.setRawTotalAmount(totalAmount);
            List<PromotionVO> validPromotionList = promotionService.getValidPromotion(); //?????????????????????????????????
            PromotionOfferInfo promotionOfferInfo = new PromotionOfferInfo();
            for (PromotionVO promotion : validPromotionList) {
                //????????????????????????
                if (promotionRequireStrategy.satisfied(promotion, saleSheetVO)) {
                    if (promotion.getDiscount() != null) {
                        PromotionOfferStrategy promotionOfferStrategy = promotionOfferStrategyContext.getResource("????????????");
                        promotionOfferInfo = PromotionOfferInfoGenerator.generate(promotionOfferInfo, promotionOfferStrategy.offer(promotion));
                    }
                    if (promotion.getVoucher() != null) {
                        PromotionOfferStrategy promotionOfferStrategy = promotionOfferStrategyContext.getResource("???????????????");
                        promotionOfferInfo = PromotionOfferInfoGenerator.generate(promotionOfferInfo, promotionOfferStrategy.offer(promotion));
                    }
                    if (promotion.getGifts() != null) {
                        PromotionOfferStrategy promotionOfferStrategy = promotionOfferStrategyContext.getResource("????????????");
                        promotionOfferInfo = PromotionOfferInfoGenerator.generate(promotionOfferInfo, promotionOfferStrategy.offer(promotion));
                    }
                }
            }

            saleSheetPO.setDiscount(promotionOfferInfo.getDiscount() == null ? BigDecimal.ONE : promotionOfferInfo.getDiscount());
            saleSheetPO.setVoucherAmount(promotionOfferInfo.getVoucher() == null ? BigDecimal.ZERO : promotionOfferInfo.getVoucher());
            //todo ?????????????????????
        }
        // ??????????????? = ??????????????? * ?????? - ???????????????
        BigDecimal finalAmount = saleSheetPO.getRawTotalAmount().multiply(saleSheetPO.getDiscount()).subtract(saleSheetPO.getVoucherAmount());
        saleSheetPO.setFinalAmount(finalAmount);
        saleSheetDao.saveSheet(saleSheetPO);
    }

    /**
     * ???????????????????????????[?????????content??????](state == null ????????????????????????)
     *
     * @param state ???????????????
     * @return ???????????????
     */
    @Override
    @Transactional
    public List<SaleSheetVO> getSaleSheetByState(SaleSheetState state) {
        // TODO
        //// DONE
        // ?????????????????????????????????????????????VO??????SaleSheetContent???
        // ?????????dao????????????????????????????????????????????????????????????
        List<SaleSheetVO> res = new ArrayList<>();
        List<SaleSheetPO> all;
        if(state == null) {
            all = saleSheetDao.findAllSheet();
        } else {
            all = saleSheetDao.findAllByState(state);
        }
        for(SaleSheetPO po: all) {
            SaleSheetVO vo = new SaleSheetVO();
            BeanUtils.copyProperties(po, vo);
            List<SaleSheetContentPO> alll = saleSheetDao.findContentBySheetId(po.getId());
            List<SaleSheetContentVO> vos = new ArrayList<>();
            for (SaleSheetContentPO p : alll) {
                SaleSheetContentVO v = new SaleSheetContentVO();
                BeanUtils.copyProperties(p, v);
                vos.add(v);
            }
            vo.setSaleSheetContent(vos);
            res.add(vo);
        }
        return res;
    }

    /**
     * ???????????????id????????????(state == "???????????????"/"????????????"/"????????????")
     * ???controller?????????????????????
     *
     * @param saleSheetId ?????????id
     * @param state       ???????????????????????????
     */
    @Override
    @Transactional
    public void approval(String saleSheetId, SaleSheetState state) {
        // TODO
        //// DONE
        // ?????????service???dao???????????????????????????????????????????????????????????????
        /* ??????????????????
            1. ????????????????????????????????????
                 1. ??????????????????
                 2. ???????????????
                 3. ???????????????
                 4. ??????????????????
            2. ?????????????????????????????????????????????????????? ????????????????????????????????????????????????
         */

        // ???????????????????????????????????????????????????????????????????????????PurchaseServiceImp.approval????????????????????????
        if(state.equals(SaleSheetState.FAILURE)) {
            SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleSheetId);
            if(saleSheet.getState() == SaleSheetState.SUCCESS) throw new RuntimeException("??????????????????");
            int effectLines = saleSheetDao.updateSheetState(saleSheetId, state);
            if(effectLines == 0) throw new RuntimeException("??????????????????");
        } else {
            SaleSheetState prevState;
            if(state.equals(SaleSheetState.SUCCESS)) {
                prevState = SaleSheetState.PENDING_LEVEL_2;
            } else if(state.equals(SaleSheetState.PENDING_LEVEL_2)) {
                prevState = SaleSheetState.PENDING_LEVEL_1;
            } else {
                throw new RuntimeException("??????????????????");
            }
            // ????????????
            int effectLines = saleSheetDao.updateSheetStateOnPrev(saleSheetId, prevState, state);
            if(effectLines == 0) throw new RuntimeException("??????????????????");
            if(state.equals(SaleSheetState.SUCCESS)) {

                // ??????????????????????????????
                    // ??????saleSheetId???????????????content -> ????????????id?????????
                    // ????????????id?????????????????????????????????recentRp
                // ???????????????????????????
                List<SaleSheetContentPO> saleSheetContent =  saleSheetDao.findContentBySheetId(saleSheetId);
                List<WarehouseOutputFormContentVO> warehouseOutputFormContentVOS = new ArrayList<>();

                for(SaleSheetContentPO content : saleSheetContent) {
                    ProductInfoVO productInfoVO = new ProductInfoVO();
                    productInfoVO.setId(content.getPid());
                    productInfoVO.setRecentRp(content.getUnitPrice());
                    productService.updateProduct(productInfoVO);

//                    here! ??????????????????????????????????????? ???????????????????????????
                    WarehouseOutputFormContentVO woContentVO = new WarehouseOutputFormContentVO();
                    woContentVO.setSalePrice(content.getUnitPrice());
                    woContentVO.setQuantity(content.getQuantity());
                    woContentVO.setRemark(content.getRemark());
                    woContentVO.setPid(content.getPid());
                    warehouseOutputFormContentVOS.add(woContentVO);
                }
                // ???????????????(??????????????????)
                // ???????????? receivable
                SaleSheetPO saleSheet = saleSheetDao.findSheetById(saleSheetId);
                CustomerPO customerPO = customerService.findCustomerById(saleSheet.getSupplier());
                customerPO.setReceivable(customerPO.getReceivable().add(saleSheet.getFinalAmount()));
                customerService.updateCustomer(customerPO);

                // ?????????????????????(????????????????????????)
                // ??????????????????????????????
                WarehouseOutputFormVO warehouseOutputFormVO = new WarehouseOutputFormVO();
                warehouseOutputFormVO.setOperator(null); // ?????????????????????(??????????????????????????????)
                warehouseOutputFormVO.setSaleSheetId(saleSheetId);
                warehouseOutputFormVO.setList(warehouseOutputFormContentVOS);
                warehouseService.productOutOfWarehouse(warehouseOutputFormVO);
            }
        }
    }

    /**
     * ?????????????????????????????????????????????????????????????????????(?????????????????????,??????????????????????????????,????????????????????????????????????????????????)
     * @param salesman ?????????????????????
     * @param beginDateStr ?????????????????????
     * @param endDateStr ?????????????????????
     * @return
     */
    @Override
    public CustomerPurchaseAmountPO getMaxAmountCustomerOfSalesmanByTime(String salesman,String beginDateStr,String endDateStr){
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date beginTime =dateFormat.parse(beginDateStr);
            Date endTime=dateFormat.parse(endDateStr);
            if(beginTime.compareTo(endTime)>0){
                return null;
            }else{
                return saleSheetDao.getMaxAmountCustomerOfSalesmanByTime(salesman,beginTime,endTime);
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ???????????????Id?????????????????????
     * @param saleSheetId ?????????Id
     * @return ?????????????????????
     */
    @Override
    public SaleSheetVO getSaleSheetById(String saleSheetId) {
        SaleSheetPO saleSheetPO = saleSheetDao.findSheetById(saleSheetId);
        if(saleSheetPO == null) return null;
        List<SaleSheetContentPO> contentPO = saleSheetDao.findContentBySheetId(saleSheetId);
        SaleSheetVO sVO = new SaleSheetVO();
        BeanUtils.copyProperties(saleSheetPO, sVO);
        List<SaleSheetContentVO> saleSheetContentVOList = new ArrayList<>();
        for (SaleSheetContentPO content:
                contentPO) {
            SaleSheetContentVO sContentVO = new SaleSheetContentVO();
            BeanUtils.copyProperties(content, sContentVO);
            saleSheetContentVOList.add(sContentVO);
        }
        sVO.setSaleSheetContent(saleSheetContentVOList);
        return sVO;
    }

}
