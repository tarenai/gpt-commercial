/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2022-08-17 09:37:55
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-04 09:26:34
 */

import React, { lazy } from "react";
import { Route, Switch, Redirect } from "react-router-dom";
import AuthRouteHoc from "@/components/AuthRouteHoc";
import Login from "@/pages/login";
// let LayoutPage = lazy(() => import("@/pages/layout/index"))
import LayoutPage from "@/pages/layout/index";
const CustomRoute = () => {
  return (
    <div style={{ width: "100%", height: "100%" }}>
      <Switch>
        <Route
          exact={true}
          path="/"
          render={() => <Redirect to="/ai/home" />}
          key="default"
        ></Route>
        <Route path="/login" component={Login} key="login"></Route>
        <Route
          path="/ai"
          component={AuthRouteHoc(LayoutPage)}
          key="/ai"
        ></Route>
      </Switch>
    </div>
  );
};

export default CustomRoute;
