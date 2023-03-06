import request from "@/network/request"
const testAPI = require("@/apis")

export const addAccount = config => request._post(testAPI.ACCOUNT_CREATE, config)
export const updateAccount = config => request._post(testAPI.ACCOUNT_UPDATE, config)
export const deleteAccount = config => request._get(testAPI.ACCOUNT_DELETE, config)
export const getAllAccounts = config => request._get(testAPI.ACCOUNT_ALL, config)
export const findAccount = config => request._get(testAPI.ACCOUNT_FIND, config)

export const approvalPayment = config => request._get(testAPI.PAYMENT_APPROVAL, config);
export const getAllPayment = config => request._get(testAPI.PAYMENT_ALL, config);
export const createPayment = config => request._post(testAPI.PAYMENT_CREATE, config);
export const approvalReceipt = config => request._get(testAPI.RECEIPT_APPROVAL, config);
export const getAllReceipt = config => request._get(testAPI.RECEIPT_ALL, config);
export const createReceipt = config => request._post(testAPI.RECEIPT_CREATE, config);

export const getAllCustomers = config => request._get(testAPI.CUSTOMERS_GET, config);

export const approvalSalary = config => request._get(testAPI.SALARY_APPROVAL, config);
export const getAllSalary = config => request._get(testAPI.SALARY_ALL, config);

export const getSaleDetail = config => request._get(testAPI.SALE_DETAIL_GET, config);
export const getProduct = config => request._get(testAPI.SALE_DETAIL_PRODUCT, config);
export const getCustomer = config => request._get(testAPI.SALE_DETAIL_CUSTOMER, config);
export const getSalesman = config => request._get(testAPI.SALE_DETAIL_SALESMAN, config);

export const getAllForms = config => request._get(testAPI.BUSINESS_PROCESS_ALL, config);
export const writeBackSale = config => request._post(testAPI.BUSINESS_PROCESS_WRITE_BACK_SALE, config);
export const writeBackSaleReturns = config => request._post(testAPI.BUSINESS_PROCESS_WRITE_BACK_SALE_RETURNS, config);
export const writeBackPurchase = config => request._post(testAPI.BUSINESS_PROCESS_WRITE_BACK_PURCHASE, config);
export const writeBackPurchaseReturns = config => request._post(testAPI.BUSINESS_PROCESS_WRITE_BACK_PURCHASE_RETURNS, config);
export const writeBackPayment = config => request._post(testAPI.BUSINESS_PROCESS_WRITE_BACK_PAYMENT, config);
export const writeBackReceipt = config => request._post(testAPI.BUSINESS_PROCESS_WRITE_BACK_RECEIPT, config);
export const writeBackSalary = config => request._post(testAPI.BUSINESS_PROCESS_WRITE_BACK_SALARY, config);

export const getBusinessSituation = config => request._get(testAPI.BUSINESS_SITUATION_ALL, config);

export const createBook = config => request._post(testAPI.BOOK_CREATE, config);
export const getBook = config => request._get(testAPI.BOOK_GET, config);
export const getId = config => request._get(testAPI.BOOK_ID, config);

export const excelSaleDetail = config => request._get(testAPI.SALE_DETAIL_EXCEL, config);
