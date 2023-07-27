/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-03-28 12:44:28
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-24 17:00:03
 */
import React, { useState, useRef, useEffect } from "react";
import {
  EyeInvisibleOutlined,
  EyeTwoTone,
  ArrowLeftOutlined,
} from "@ant-design/icons";
import styles from "./index.module.less";
import { messageFn } from "@/utils";
import { Input, Space, Button } from "antd";
import logo from "../../../public/assets/imgs/logo.svg";
import {
  authorizationUrl,
  wechatTestLogin,
  checkToken,
  userLogin,
  systemConfig,
  wxLoginState,
} from "@/api/login";
import QRCode from "qrcode.react";
import { setCookie, queryString, getCookie, setSessionStorage } from "@/utils";
import _ from "lodash";
import "./index.css";
import SharkBtn from "@/components/SharkBtn";
import wxLogo from "../../../public/assets/imgs/wx-logo.svg";
import phoneIcon from "../../../public/assets/imgs/phone.svg";
import UserAgreement from "./components/UserAgreement";
import PrivacyStatement from "./components/PrivacyStatement";
import UserRegister from "./components/UserRegister";
import ChangePwd from "@/components/HeadComponent/components/ChangePwd";
import BgCanvas from "@/components/BgCanvas";
const Login = ({ history }) => {
  const [resultUrl, setResult] = useState("");
  const [wxCode, setWxCode] = useState(1);

  // 手机号
  const [phoneNumber, setPhoneNumber] = useState("");
  // 邮箱账号
  const [mailNumber, setMailNumber] = useState("");
  const [password, setPassword] = useState("");
  // 手机号
  const [phonePassword, setPhonePassword] = useState("");
  // 用户协议 与 隐私声明
  const userAgreementRef = useRef(null);
  const privacyStatementRef = useRef(null);
  // 拉新code
  const inviteCode = useRef("");

  // 用户注册
  const userRegisterRef = useRef(null);

  // 忘记密码
  const changePwdRef = useRef(null);

  // 系统配置
  const [sysConfig, setSysConfig] = useState({});
  const wxLoginTimer = useRef(null);
  const wxLoginSceneStr = useRef(null);
  const [wxLoginQcodeTimeout, setwxLoginQcodeTimeout] = useState(false);
  // 登录类型  wx or phone
  // 1 邮箱 2手机号 3微信
  const [loginType, setLoginType] = useState(null);

  // 获取系统配置
  const systemConfigFn = async () => {
    try {
      let res = await systemConfig();
      if (res.code === 200) {
        let resData = res.result;
        setSysConfig(resData);
        setSessionStorage("sysConfig", JSON.stringify(resData));
        var link =
          document.querySelector("link[rel*='icon']") ||
          document.createElement("link");

        link.type = "image/x-icon";

        link.rel = "shortcut icon";

        link.href = resData?.iconUrl || "";

        document.getElementsByTagName("head")[0].appendChild(link);
        document.title = resData?.webName || "";
      }
    } catch (error) {
      console.log(error);
    }
  };

  // 页面加载完毕后 获取url中的拉新code
  useEffect(() => {
    let urlInviteCode = queryString("code");
    if (urlInviteCode) {
      inviteCode.current = urlInviteCode;
    }

    let tokenValue = getCookie("tokenValue");
    if (tokenValue) {
      checkTokenFn();
    }
    // 获取系统配置
    systemConfigFn();

    // 组件销毁 消除定时器
    return () => {
      clearInterval(wxLoginTimer.current);
    };

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  // 获取微信授权
  const authorizationUrlFn = async () => {
    try {
      setLoginType(3);
      setwxLoginQcodeTimeout(false);
      if (wxLoginTimer.current) {
        clearInterval(wxLoginTimer.current);
      }
      let params = {
        inviteCode: inviteCode.current,
      };
      let res = await authorizationUrl(params);
      if (res.code === 200) {
        let resData = res.result;
        setResult(resData.url);
        wxLoginSceneStr.current = resData.sceneStr;

        wxLoginTimer.current = setInterval(() => {
          pollingCheckWxLoginState();
        }, 1000);
      }
    } catch (error) {
      console.log(error);
    }
  };

  // 刷新微信验证码
  const refreshWxCode = () => {
    authorizationUrlFn();
  };

  // 轮询查询微信扫码登录状态
  const pollingCheckWxLoginState = async () => {
    try {
      let params = {
        sceneStr: wxLoginSceneStr.current,
      };
      let res = await wxLoginState(params);
      if (res.code === 200) {
        setCookie("tokenName", "satoken");
        setCookie("tokenValue", res.result);
        history.replace("/ai/home");
      } else if (res.code === 4009) {
        setwxLoginQcodeTimeout(true);
        if (wxLoginTimer.current) {
          clearInterval(wxLoginTimer.current);
        }
      }
    } catch (error) {
      console.log(error);
    }
  };

  /**
   * @description: 监听页面地址发生变化
   * @return {*}
   * @author: jinglin.gao
   */
  history.listen((path) => {
    // console.log("路径变化", path);
  });

  /**
   * @description: 微信登录
   * @return {*}
   * @author: jinglin.gao
   */
  const wechatLoginFn = async () => {
    try {
      let data = {
        code: wxCode,
        inviteCode: inviteCode.current,
      };
      let res = await wechatTestLogin(data);

      if (res.code === 200) {
        const { result } = res;
        setCookie("tokenName", result.tokenName);
        setCookie("tokenValue", result.tokenValue);

        history.replace("/ai/home");
      }
      console.log(res, "222");
    } catch (error) {
      console.log(error);
    }
  };

  /**
   * @description: 用户协议
   * @param {*} e
   * @return {*}
   * @author: jinglin.gao
   */
  const userAgreementClick = () => {
    userAgreementRef.current.getPage();
  };

  const privacyStatementClick = () => {
    privacyStatementRef.current.getPage();
  };

  /**
   * @description: 邮箱
   * @return {*}
   * @author: jinglin.gao
   */
  const accountLogin = () => {
    setLoginType(1);
  };

  /**
   * @description: 校验token是否过期
   * @return {*}
   * @author: jinglin.gao
   */

  const checkTokenFn = async () => {
    try {
      let res = await checkToken();
      if (res.code === 200) {
        history.replace("/ai/home");
      }
      console.log(res);
    } catch (error) {
      console.log(error);
    }
  };

  //邮箱用户注册
  const mailRegister = () => {
    userRegisterRef.current.getPage(1);
  };

  /**
   * @description: 邮箱忘记密码
   * @return {*}
   * @author: jinglin.gao
   */
  const forgotPassword = () => {
    // 用于区分是忘记密码还是修改密码
    changePwdRef.current.getPage(0);
  };

  /**
   * @description:
   * @return {*} 0代表 忘记密码 1 代表修改密码
   * @author: jinglin.gao
   */
  const phoneForgotPassword = () => {
    // 用于区分是忘记密码还是修改密码
    changePwdRef.current.getPage(0, 2);
  };

  // 手机用户注册
  const phoneNumberRegister = () => {
    userRegisterRef.current.getPage(2);
  };

  /**
   * @description: 邮箱登录
   * @return {*}
   * @author: jinglin.gao
   */
  const mailLoginFn = async () => {
    if (!mailNumber) {
      messageFn({
        type: "error",
        content: "请输入账号",
      });
      return;
    } else if (!password) {
      messageFn({
        type: "error",
        content: "请输入密码",
      });

      return;
    }
    try {
      let data = {
        accountNum: mailNumber,
        password: password,
        type: 1,
        inviteCode: inviteCode.current,
      };
      let res = await userLogin(data);
      if (res.code === 200) {
        messageFn({
          type: "success",
          content: "验证成功",
        });

        setCookie("tokenName", "satoken");
        setCookie("tokenValue", res.result);
        history.replace("/ai/home");

        // setSessionStorage("userInfo", res.result);
      } else {
        messageFn({
          type: "error",
          content: res.message,
        });
      }
    } catch (error) {
      console.log(error);
    }
  };

  /**
   * @description: 手机号登录
   * @return {*}
   * @author: jinglin.gao
   */
  const phoneLoginFn = async () => {
    console.log(phonePassword, "phonePassword");
    if (!phoneNumber) {
      messageFn({
        type: "error",
        content: "请输入账号",
      });
      return;
    } else if (!phonePassword) {
      messageFn({
        type: "error",
        content: "请输入密码",
      });

      return;
    }
    try {
      let data = {
        accountNum: phoneNumber,
        password: phonePassword,
        type: 2,
        inviteCode: inviteCode.current,
      };
      let res = await userLogin(data);
      if (res.code === 200) {
        messageFn({
          type: "success",
          content: "验证成功",
        });

        setCookie("tokenName", "satoken");
        setCookie("tokenValue", res.result);
        history.replace("/ai/home");

        // setSessionStorage("userInfo", res.result);
      } else {
        messageFn({
          type: "error",
          content: res.message,
        });
      }
    } catch (error) {
      console.log(error);
    }
  };

  // 返回首页
  const backUp = () => {
    setLoginType(null);
  };

  return (
    <div className={styles.loginWarp}>
      {/* canvas */}
      <BgCanvas></BgCanvas>
      <div className="login-content">
        {!loginType ? (
          <div className="loginBox">
            <img className="logo" src={sysConfig?.iconUrl || logo} alt="" />
            <div className="login_content_webName">
              {sysConfig?.webName || "Genius"}
            </div>
            <h1 className="title">{sysConfig?.subTitle || "科技改变生活"}</h1>

            <div className="login_method-box">
              {sysConfig.loginType === 3 ? (
                <SharkBtn
                  onClick={authorizationUrlFn}
                  icon={wxLogo}
                  name={"扫码登录"}
                ></SharkBtn>
              ) : sysConfig.loginType === 1 ? (
                <SharkBtn
                  onClick={accountLogin}
                  icon={phoneIcon}
                  name={"邮箱登录"}
                ></SharkBtn>
              ) : sysConfig.loginType === 2 ? (
                <SharkBtn
                  onClick={() => setLoginType(2)}
                  icon={phoneIcon}
                  name={"手机号登录"}
                ></SharkBtn>
              ) : (
                ""
              )}
            </div>

            <div className="userAgreementInfoBox">
              <p className="info_item" onClick={userAgreementClick}>
                用户协议
              </p>
              <p className="info_item" onClick={privacyStatementClick}>
                免责声明
              </p>
            </div>
          </div>
        ) : (
          <div className="typeLoginBox">
            {/* 微信扫码登录 */}
            <ArrowLeftOutlined onClick={backUp} className="backUp" />
            {loginType === 3 ? (
              <div className="wxLoginBox">
                <h1 className="title">微信扫码登录</h1>
                {resultUrl ? (
                  <>
                    {" "}
                    <QRCode
                      id="qrCode"
                      value={resultUrl}
                      size={200} // 二维码的大小
                      fgColor="#000000" // 二维码的颜色
                    />
                    {wxLoginQcodeTimeout ? (
                      <p className="refreshWxCode" onClick={refreshWxCode}>
                        二维码已失效，点击重新获取二维码
                      </p>
                    ) : (
                      ""
                    )}
                  </>
                ) : (
                  ""
                )}
              </div>
            ) : loginType === 1 ? (
              // 邮箱账号密码登录
              <div className="phoneLogin">
                <div className="phone_login-warp">
                  <div className="title">账号密码登录</div>
                  <div className="content-box">
                    <Space direction="vertical">
                      <Input
                        maxLength={30}
                        value={mailNumber}
                        onChange={(e) => setMailNumber(e.target.value)}
                        onPressEnter={() => {
                          mailLoginFn();
                        }}
                        placeholder="请输入账号"
                      />
                      <Input.Password
                        onPressEnter={() => {
                          mailLoginFn();
                        }}
                        maxLength={30}
                        minLength={6}
                        placeholder="请输入密码"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        iconRender={(visible) =>
                          visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />
                        }
                      />
                    </Space>

                    <div
                      className="submitBtnText"
                      onClick={_.debounce(mailLoginFn, 500)}
                    >
                      登录
                    </div>
                    <div className="tools_btns">
                      <span onClick={mailRegister} className="btns-item">
                        邮箱注册
                      </span>
                      <span onClick={forgotPassword} className="btns-item">
                        忘记密码
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            ) : (
              // 手机号账号密码登录
              <div className="phoneLogin">
                <div className="phone_login-warp">
                  <div className="title">手机号登录</div>
                  <div className="content-box">
                    <Space direction="vertical">
                      <Input
                        onPressEnter={() => {
                          phoneLoginFn();
                        }}
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)}
                        placeholder="请输入账号"
                      />
                      <Input.Password
                        onPressEnter={() => {
                          phoneLoginFn();
                        }}
                        maxLength={30}
                        minLength={6}
                        onChange={(e) => setPhonePassword(e.target.value)}
                        placeholder="请输入密码"
                        iconRender={(visible) =>
                          visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />
                        }
                      />
                    </Space>

                    <div
                      className="submitBtnText"
                      onClick={_.debounce(phoneLoginFn, 500)}
                    >
                      登录
                    </div>
                    <div className="tools_btns">
                      <span onClick={phoneNumberRegister} className="btns-item">
                        手机号注册
                      </span>
                      <span onClick={phoneForgotPassword} className="btns-item">
                        忘记密码
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            )}
          </div>
        )}
      </div>

      {/* 互联网公安备案号 */}
      {sysConfig?.filingNumber ? (
        <div className="login_bottom-info">
          <a href="https://beian.miit.gov.cn/#/Integrated/index">
            互联网ICP备案: {sysConfig.filingNumber}
          </a>
        </div>
      ) : (
        ""
      )}

      {/* 用户协议 */}
      <UserAgreement ref={userAgreementRef}></UserAgreement>

      {/* 隐私声明 */}

      <PrivacyStatement ref={privacyStatementRef}></PrivacyStatement>

      {/* 用户注册 */}
      <UserRegister ref={userRegisterRef}></UserRegister>

      {/* 忘记密码 */}
      <ChangePwd ref={changePwdRef}></ChangePwd>
    </div>
  );
};

export default Login;
