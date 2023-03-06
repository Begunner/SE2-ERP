import request from "@/network/request"
const testAPI = require("@/apis")

export const getCheckByTime = config => request._get(testAPI.CHECK_TIME, config);
export const getAllCheck = config => request._get(testAPI.CHECK_ALL, config);
export const getCheckToday = config => request._get(testAPI.CHECK_TODAY, config);

export const postCheckToday = config => request._post(testAPI.CHECK_POST, config);
