import request from "@/network/request"
const testAPI = require("@/apis")


export const getAllPromotion = config => request._get(testAPI.PROMOTION_ALL, config)
export const createPromotion = config => request._post(testAPI.PROMOTION_CREATE, config)

export const getAllBonus = config => request._get(testAPI.BONUS_ALL, config)
export const changeBonus = config => request._get(testAPI.BONUS_CHANGE, config)

