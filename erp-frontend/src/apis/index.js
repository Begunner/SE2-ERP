//TEST
const TEST_GET = "/api/test/get";
const TEST_POST = "/api/test/post";

const AUTH = "/api/user/auth"
const LOGIN = "/api/user/login"
const REGISTER = '/api/user/register'

// 商品分类管理
const COMMODITY_CLASSIFICATION_ALL = '/api/category/queryAll'
const COMMODITY_CLASSIFICATION_CREATE = '/api/category/create'
const COMMODITY_CLASSIFICATION_UPDATE = '/api/category/update'
const COMMODITY_CLASSIFICATION_DELETE = '/api/category/delete'

// 商品管理
const COMMODITY_ALL = '/api/product/queryAll';
const COMMODITY_CREATE = '/api/product/create';
const COMMODITY_UPDATE = '/api/product/update';
const COMMODITY_DELETE = '/api/product/delete';


const WAREHOUSE_INPUT = '/api/warehouse/input';
const WAREHOUSE_OUTPUT_PRE = '/api/warehouse/product/count';
const WAREHOUSE_OUTPUT = '/api/warehouse/output';
const WAREHOUSE_GET_INPUTSHEET = '/api/warehouse/inputSheet/state';
const WAREHOUSE_GET_OUTPUTSHEET = '/api/warehouse/outputSheet/state';
const WAREHOUSE_IO_DEATIL_BY_TIME = '/api/warehouse/sheetContent/time';
const WAREHOUSE_IPQ_BY_TIME = '/api/warehouse/inputSheet/time/quantity';
const WAREHOUSE_OPQ_BY_TIME = '/api/warehouse/outputSheet/time/quantity';
const WAREHOUSE_OUTPUTSHEET_APPROVE = '/api/warehouse/outputSheet/approve';
const WAREHOUSE_INPUTSHEET_APPROVE = '/api/warehouse/inputSheet/approve';
const WAREHOUSE_DAILY_COUNT = '/api/warehouse/warehouse/counting';
const WAREHOUSE_EXCEL = '/api/warehouse/warehouse/excel';

// 销售管理
// 进货管理
const PURCHASE_ALL = '/api/purchase/sheet-show';
const PURCHASE_FIND_SHEET_BY_ID = '/api/purchase/find-sheet';
const PURCHASE_CREATE = '/api/purchase/sheet-make';
const PURCHASE_FIRST_APPROVAL = '/api/purchase/first-approval';
const PURCHASE_SECOND_APPROVAL = '/api/purchase/second-approval';
// 进货退货管理
const PURCHASE_RETURN_ALL = '/api/purchase-returns/sheet-show';
const PURCHASE_RETURN_CREATE = '/api/purchase-returns/sheet-make';
const PURCHASE_RETURN_FIRST_APPROVAL = '/api/purchase-returns/first-approval';
const PURCHASE_RETURN_SECOND_APPROVAL = '/api/purchase-returns/second-approval';
// 销售管理
const SALE_ALL = '/api/sale/sheet-show';
const SALE_CREATE = '/api/sale/sheet-make';
const SALE_FIRST_APPROVAL = '/api/sale/first-approval';
const SALE_SECOND_APPROVAL = '/api/sale/second-approval';
const SALE_FIND_SHEET_BY_ID = '/api/sale/find-sheet';
// 销售退货管理
const SALE_RETURN_ALL = '/api/sale-returns/sheet-show';
const SALE_RETURN_CREATE = '/api/sale-returns/sheet-make';
const SALE_RETURN_FIRST_APPROVAL = '/api/sale-returns/first-approval';
const SALE_RETURN_SECOND_APPROVAL = '/api/sale-returns/second-approval';
// 客户管理
const CUSTOMER_QUERY = '/api/customer/findByType';
const SALE_PURCHASE_ALL = '/api/purchase/sheet-show';
const SALE_PURCHASE_CREATE = '/api/purchase/sheet-make';
const SALE_CUSTOMER_QUERY = '/api/customer/findByType';
const SALE_CUSTOMER_MAX = '/api/sale/maxAmountCustomer';
const CUSTOMER_ADD = '/api/customer/add';
const CUSTOMER_DELETE = '/api/customer/delete';
// 财务管理
const ACCOUNT_CREATE = 'api/account/create'
const ACCOUNT_UPDATE = 'api/account/update'
const ACCOUNT_DELETE = 'api/account/delete'
const ACCOUNT_ALL = 'api/account/queryAll'
const ACCOUNT_FIND = 'api/account/findByName'
const PAYMENT_APPROVAL = 'api/payment/approval'
const PAYMENT_ALL = 'api/payment/sheet-show'
const PAYMENT_CREATE = 'api/payment/sheet-make'
const RECEIPT_APPROVAL = 'api/receipt/approval'
const RECEIPT_ALL = 'api/receipt/sheet-show'
const RECEIPT_CREATE = 'api/receipt/sheet-make'
const CUSTOMERS_GET = 'api/customer/findAll'
const SALARY_APPROVAL = 'api/salary-sheet/approval'
const SALARY_ALL = 'api/salary-sheet/sheet-show'
const SALE_DETAIL_GET = 'api/business/sale-details/show'
const SALE_DETAIL_SALESMAN = 'api/business/sale-details/salesman-set'
const SALE_DETAIL_PRODUCT = 'api/business/sale-details/product-set'
const SALE_DETAIL_CUSTOMER = 'api/business/sale-details/supplier-set'
const SALE_DETAIL_EXCEL = 'api/business/sale-details/excel'
const BUSINESS_PROCESS_ALL = 'api/business/business-process/show'
const BUSINESS_PROCESS_WRITE_BACK_SALE = 'api/business/business-process/write-back/sale-sheet'
const BUSINESS_PROCESS_WRITE_BACK_SALE_RETURNS = 'api/business/business-process/write-back/sale-returns-sheet'
const BUSINESS_PROCESS_WRITE_BACK_PURCHASE = 'api/business/business-process/write-back/purchase-sheet'
const BUSINESS_PROCESS_WRITE_BACK_PURCHASE_RETURNS = 'api/business/business-process/write-back/purchase-returns-sheet'
const BUSINESS_PROCESS_WRITE_BACK_PAYMENT = 'api/business/business-process/write-back/payment-sheet'
const BUSINESS_PROCESS_WRITE_BACK_RECEIPT = 'api/business/business-process/write-back/receipt-sheet'
const BUSINESS_PROCESS_WRITE_BACK_SALARY = 'api/business/business-process/write-back/salary-sheet'
const BUSINESS_SITUATION_ALL = 'api/business/business-situation/show'
const BOOK_GET = 'api/business/book/get'
const BOOK_CREATE = 'api/business/book/create'
const BOOK_ID = 'api/business/book/ids'

// 人力资源管理
const EMPLOYEE_CREATE = 'api/employee/induction'
const EMPLOYEE_UPDATE = 'api/employee/update'
const EMPLOYEE_ALL = 'api/employee/queryAll'
// 总经理
const PROMOTION_ALL = 'api/promotion/show/all'
const PROMOTION_CREATE = 'api/promotion/create'
const BONUS_ALL = 'api/salary-sheet/bonus/queryAll'
const BONUS_CHANGE = 'api/salary-sheet/bonus/set'
// 员工打卡
const CHECK_TIME = 'api/clock-in/getByDate-personal'
const CHECK_ALL = 'api/clock-in/queryAll-personal'
const CHECK_TODAY = 'api/clock-in/get-personal'
const CHECK_POST = 'api/clock-in/clock-in'


module.exports = {
  TEST_GET,
  TEST_POST,

  AUTH,
  LOGIN,
  REGISTER,
  COMMODITY_CLASSIFICATION_ALL,
  COMMODITY_CLASSIFICATION_CREATE,
  COMMODITY_CLASSIFICATION_UPDATE,
  COMMODITY_CLASSIFICATION_DELETE,

  COMMODITY_ALL,
  COMMODITY_CREATE,
  COMMODITY_UPDATE,
  COMMODITY_DELETE,

  WAREHOUSE_INPUT,
  WAREHOUSE_OUTPUT_PRE,
  WAREHOUSE_OUTPUT,
  WAREHOUSE_GET_INPUTSHEET,
  WAREHOUSE_GET_OUTPUTSHEET,
  WAREHOUSE_IO_DEATIL_BY_TIME,
  WAREHOUSE_IPQ_BY_TIME,
  WAREHOUSE_OPQ_BY_TIME,
  WAREHOUSE_OUTPUTSHEET_APPROVE,
  WAREHOUSE_INPUTSHEET_APPROVE,
  WAREHOUSE_DAILY_COUNT,
  WAREHOUSE_EXCEL,

  PURCHASE_ALL,
  PURCHASE_CREATE,
  PURCHASE_FIRST_APPROVAL,
  PURCHASE_SECOND_APPROVAL,
  PURCHASE_RETURN_ALL,
  PURCHASE_RETURN_CREATE,
  PURCHASE_RETURN_FIRST_APPROVAL,
  PURCHASE_RETURN_SECOND_APPROVAL,
  PURCHASE_FIND_SHEET_BY_ID,

  SALE_ALL,
  SALE_CREATE,
  SALE_FIRST_APPROVAL,
  SALE_SECOND_APPROVAL,
  SALE_CUSTOMER_QUERY,
  SALE_CUSTOMER_MAX,
  SALE_FIND_SHEET_BY_ID,

  SALE_RETURN_ALL,
  SALE_RETURN_CREATE,
  SALE_RETURN_FIRST_APPROVAL,
  SALE_RETURN_SECOND_APPROVAL,

  CUSTOMER_QUERY,
  CUSTOMER_ADD,
  CUSTOMER_DELETE,

  ACCOUNT_CREATE,
  ACCOUNT_DELETE,
  ACCOUNT_ALL,
  ACCOUNT_UPDATE,
  ACCOUNT_FIND,

  PAYMENT_APPROVAL,
  PAYMENT_ALL,
  PAYMENT_CREATE,

  RECEIPT_APPROVAL,
  RECEIPT_ALL,
  RECEIPT_CREATE,

  CUSTOMERS_GET,

  SALARY_APPROVAL,
  SALARY_ALL,

  SALE_DETAIL_GET,
  SALE_DETAIL_PRODUCT,
  SALE_DETAIL_CUSTOMER,
  SALE_DETAIL_SALESMAN,
  SALE_DETAIL_EXCEL,

  BUSINESS_PROCESS_ALL,
  BUSINESS_PROCESS_WRITE_BACK_SALE,
  BUSINESS_PROCESS_WRITE_BACK_SALE_RETURNS,
  BUSINESS_PROCESS_WRITE_BACK_PURCHASE,
  BUSINESS_PROCESS_WRITE_BACK_PURCHASE_RETURNS,
  BUSINESS_PROCESS_WRITE_BACK_PAYMENT,
  BUSINESS_PROCESS_WRITE_BACK_RECEIPT,
  BUSINESS_PROCESS_WRITE_BACK_SALARY,

  BUSINESS_SITUATION_ALL,

  BOOK_GET,
  BOOK_ID,
  BOOK_CREATE,

  EMPLOYEE_CREATE,
  EMPLOYEE_UPDATE,
  EMPLOYEE_ALL,

  CHECK_TIME,
  CHECK_TODAY,
  CHECK_ALL,
  CHECK_POST,

  PROMOTION_ALL,
  PROMOTION_CREATE,

  BONUS_CHANGE,
  BONUS_ALL,
};
