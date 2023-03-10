package com.nju.edu.erp.model.vo.promotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromotionPackageVO {
    /**
     * ååid
     */
    private String productId;
    /**
     * æ°é
     */
    private Integer quantity;
}
