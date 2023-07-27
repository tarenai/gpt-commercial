/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-03 09:26:09
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-04 12:29:56
 */
import React, { Component } from "react";
import { useHistory } from "react-router-dom";
const AuthRouteHoc = (ChildComponent) => {
  const history = useHistory();
  return class extends Component {
    constructor(props) {
      super(props);
      this.state = {
        userId: window.localStorage.getItem("userId"),
      };
    }

    backHome() {
      history.replace("/login");
    }

    render() {
      return (
        <>
          {/* {this.state.userId ? (
            <ChildComponent />
          ) : (
            "用户信息已过期，请重新登录"
          )} */}
          <ChildComponent />
        </>
      );
    }
  };
};

export default AuthRouteHoc;
