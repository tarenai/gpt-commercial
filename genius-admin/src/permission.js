/*
 * @Description: 
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2022-08-18 16:42:46
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-30 10:28:34
 */
import router from "./router";
import store from "./store";
import {
  Message
} from "element-ui";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
import {
  getToken
} from "@/utils/auth"; // get token from cookie
import getPageTitle from "@/utils/get-page-title";

NProgress.configure({
  showSpinner: false
}); // NProgress Configuration

const whiteList = ["/login"]; // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  // start progress bar
  NProgress.start();

  // set page title
  document.title = getPageTitle(to.meta.title);
  next();
});

router.afterEach(() => {
  // finish progress bar
  NProgress.done();
});
