/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2022-08-17 08:47:20
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-05 13:18:44
 */

import React, { useState, lazy } from "react";
import { Layout, ConfigProvider } from "antd";
import zhCN from "antd/es/locale/zh_CN";
import "../../App.less";
import { Route, Switch } from "react-router-dom";
import HeadComponent from "@/components/HeadComponent";

import Home from "@/pages/Home";
import Commodity from "@/pages/commodity";
// 引入store
import store from "@/store/store.js";
// 引入react-redux
// react-redux 主要通过两个方法实现组件到store的关联
// Provider 通过在根组件进行包裹 将store = {store} 我们定义好的store传入。
import { Provider } from "react-redux";

const { Content } = Layout;

const LayoutPage = (props) => {
  console.log(props, "propsprops");
  const [headMenu] = useState([]);

  return (
    <Provider store={store}>
      <ConfigProvider locale={zhCN}>
        <Layout className="App">
          {/* 头部 */}
          {/*  */}
          <HeadComponent menu={headMenu}></HeadComponent>
          <Layout className="LayoutContent">
            <Content className="site-layout-background">
              {/* 路由注册 */}
              <Switch>
                <Route path="/ai/home" component={Home} key="/ai/home"></Route>

                <Route
                  path="/ai/commodity"
                  component={Commodity}
                  key="/ai/commodity"
                ></Route>
              </Switch>
            </Content>
          </Layout>
        </Layout>
      </ConfigProvider>
    </Provider>
  );
};

export default LayoutPage;
