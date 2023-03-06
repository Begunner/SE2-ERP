package com.nju.edu.erp.model.vo.payment;

import com.nju.edu.erp.enums.sheetState.PaymentSheetState;
import com.nju.edu.erp.model.vo.SheetVO;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentSheetVO extends SheetVO {
    /**
     * 付款单单据编号，格式为：FKD-yyyyMMdd-xxxxx，新建单据时前端传null
     * 在父类SheetVO中
     */
    //private String id;
    /**
     * 客户/销售商/供应商
     */
    private Integer supplier;
    /**
     * 操作员，为当前操作者，前端传null
     */
    private String operator;
    /**
     * 付款总额，前端传null
     */
    private BigDecimal totalAmount;
    /**
     * 备注
     */
    private String remark;
    /**
     * 单据状态,前端传null
     */
    private PaymentSheetState state;
    /**
     * 创建时间，前端传null
     * 在父类SheetVO中
     */
//    private Date createTime;
    /**
     * 转账列表信息
     */
    private List<PaymentSheetContentVO> paymentSheetContent;
}
