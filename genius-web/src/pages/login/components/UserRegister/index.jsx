/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-08 10:06:25
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-24 16:28:45
 */
import React, {
  useState,
  forwardRef,
  useImperativeHandle,
  useRef,
  useEffect,
} from "react";
import { Input } from "antd";
import _ from "lodash";
import styles from "./index.module.less";
import { CloseOutlined } from "@ant-design/icons";
import UserAttestation from "../UserAttestation";
import { messageFn } from "@/utils";
import { sendSms, smsSignUp } from "@/api/login";
const UserRegister = forwardRef((props, ref) => {
  const [pageState, setPageState] = useState(false);
  // 手机号
  const [phoneNumber, setPhoneNumber] = useState("");
  // 密码
  const [password, setPassword] = useState("");
  // 短信验证码
  const [smsVerificationCode, setSmsVerificationCode] = useState("");

  // 验证码状态
  const [smsState, setSmsState] = useState(false);

  // 验证码倒计时
  const [countdownSmsTimer, setCountdownSmsTimer] = useState(60);

  const countdownSmsTimerRef = useRef(null);
  // 图片验证码
  const [imgVerificationCode, setImgVerificationCode] = useState("");

  const userAttestationRef = useRef(null);

  // 邮箱账号
  const [mailNumber, setMailNumber] = useState("");

  // 注册类型
  const rigistType = useRef("");
  // 监听图片验证码变化后 调用短信发送
  useEffect(() => {
    if (imgVerificationCode) {
      getSmsCode();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [imgVerificationCode]);

  useEffect(() => {
    if (countdownSmsTimerRef.current) {
      clearInterval(countdownSmsTimerRef.current);
    }
  }, []);

  useImperativeHandle(ref, () => {
    return {
      getPage,
    };
  });

  /**
   * @description: 弹框展示
   * @return {*}
   * @author: jinglin.gao
   */
  const getPage = (type) => {
    setPageState(true);
    rigistType.current = type; // 设置绑定的类型
  };

  /**
   * @description: 弹框隐藏
   * @return {*}
   * @author: jinglin.gao
   */

  const hidePage = () => {
    setPageState(false);
  };

  /**
   * @description: 校验人机操作
   * @return {*}
   * @author: jinglin.gao
   */
  const getVerificationCode = () => {
    if (!mailNumber) {
      messageFn({
        type: "error",
        content: "请输入账号",
      });

      return;
    }

    userAttestationRef.current.getPage(rigistType.current);
  };

  /**
   * @description: 获取短信验证码
   * @return {*}
   * @author: jinglin.gao
   */
  const getSmsCode = async () => {
    try {
      let data = {
        sendAccount: mailNumber,
        type: "1",
        sendType: rigistType.current,
      };
      let res = await sendSms(data);
      if (res.code === 200) {
        messageFn({
          type: "success",
          content: "验证码发送成功",
        });
        setSmsState(true);
        // 60S打倒计时
        countdownSmsTimerFn();
      } else {
        messageFn({
          type: "error",
          content: res.message,
        });
        setSmsState(false);
      }
      console.log(res, "11111");
    } catch (error) {
      console.log(error);
    }
  };

  // 倒计时开始
  const countdownSmsTimerFn = () => {
    if (countdownSmsTimerRef.current) {
      clearInterval(countdownSmsTimerRef.current);
    }
    let count = 60;
    setCountdownSmsTimer(60);
    countdownSmsTimerRef.current = setInterval(() => {
      count--;

      if (count <= 0) {
        setSmsState(false);
        if (countdownSmsTimerRef.current) {
          clearInterval(countdownSmsTimerRef.current);
        }
      }
      setCountdownSmsTimer(count);
    }, 1000);
  };

  const submit = async () => {
    if (!mailNumber) {
      messageFn({
        type: "error",
        content: "请输入账号",
      });
      return;
    } else if (!smsVerificationCode) {
      messageFn({
        type: "error",
        content: "请输入验证码",
      });
      return;
    } else if (!password) {
      messageFn({
        type: "error",
        content: "请输入密码",
      });
      return;
    } else if (password.length < 6) {
      messageFn({
        type: "error",
        content: "密码长度需大于6位",
      });
      return;
    }

    try {
      let data = {
        accountNum: mailNumber,
        type: rigistType.current,
        imageVerificationCode: imgVerificationCode,
        smsCode: smsVerificationCode,
        password: password,
      };
      let res = await smsSignUp(data);
      if (res.code === 200) {
        hidePage();
        messageFn({
          type: "success",
          content: "恭喜您，注册成功",
        });
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

  return (
    <>
      {pageState ? (
        <div className={styles.custom_dialog}>
          <div className="custom_dialog-warp animate__animated animate__flipInX">
            <div className="custom_dialog-head">
              <span className="title">注册账号</span>
              <CloseOutlined
                onClick={hidePage}
                className="closeBtn"
                twoToneColor="#fff"
              ></CloseOutlined>
            </div>

            <div className="custom_dialog-content">
              <div className="content-item">
                <Input
                  maxLength={30}
                  value={mailNumber}
                  onChange={(e) => setMailNumber(e.target.value)}
                  placeholder="请输入账号"
                />
              </div>
              <div className="verification_code-item">
                <Input
                  maxLength={30}
                  minLength={6}
                  value={smsVerificationCode}
                  onChange={(e) => setSmsVerificationCode(e.target.value)}
                  placeholder="请输入验证码"
                />
                {!smsState ? (
                  <span className="code-item" onClick={getVerificationCode}>
                    获取验证码
                  </span>
                ) : (
                  <span className="code-item">{countdownSmsTimer}s</span>
                )}
              </div>
              <div className="content-item">
                <Input.Password
                  maxLength={30}
                  minLength={6}
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="请输入6~30位密码"
                />
              </div>

              <div className="vcerification_code-img-box">
                <div className="user_detail_bottom">
                  <div
                    onClick={_.debounce(submit, 500)}
                    className="submitBtnText"
                  >
                    注册
                  </div>
                </div>
              </div>
            </div>
          </div>

          <UserAttestation
            setImgVerificationCode={setImgVerificationCode}
            phoneNumber={mailNumber}
            ref={userAttestationRef}
          ></UserAttestation>
        </div>
      ) : (
        ""
      )}
    </>
  );
});

export default UserRegister;
