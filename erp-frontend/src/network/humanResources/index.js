import request from "@/network/request"
const testAPI = require("@/apis")

export const addEmployee = config => request._post(testAPI.EMPLOYEE_CREATE, config)
export const updateEmployee = config => request._post(testAPI.EMPLOYEE_UPDATE, config)
export const getAllEmployee = config => request._get(testAPI.EMPLOYEE_ALL, config)
