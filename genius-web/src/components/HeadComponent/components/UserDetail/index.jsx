import React, {
  useState,
  forwardRef,
  useImperativeHandle,
  useRef,
} from "react";

import { useHistory } from "react-router-dom";
import styles from "./index.module.less";
import { CloseOutlined } from "@ant-design/icons";
import { Button } from "antd";
import { getMemberUserInfo } from "@/api/user";
import InviteGiftUrlInfo from "@/components/InviteGiftUrlInfo";
import { useSelector } from "react-redux";
import { logout } from "@/api/login";
import { removeCookie, messageFn } from "@/utils";
import ChangePwd from "../ChangePwd/index";
import userDeafultImg from "../../../../../public/assets/imgs/userDeafultImg.svg";
import Propose from "../Propose";
const UserDetail = forwardRef((props, ref) => {
  const [pageState, setPageState] = useState(false);
  const inviteGiftUrlInfoRef = useRef(null);
  const changePwdRef = useRef(null);
  const history = useHistory();
  const userInfo = useSelector((state) => state.userInfo);
  const [memberUserInfo, setMemberUserInfo] = useState({});
  const proposeRef = useRef(null);
  useImperativeHandle(ref, () => {
    return {
      getPage,
    };
  });

  /**
   * @description:退出登录
   * @return {*}
   * @author: jinglin.gao
   */
  const backLogin = async () => {
    try {
      let res = await logout();
      if (res.code === 200) {
        messageFn({
          type: "success",
          content: "退出登录成功",
        });
        history.replace("/login");
        removeCookie("satoken");
        removeCookie("tokenName");
        removeCookie("tokenValue");
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
   * @description: 弹框展示
   * @return {*}
   * @author: jinglin.gao
   */
  const getPage = () => {
    setPageState(true);
    // getUserInfoFn();
    getMemberUserInfoFn();

    console.log(userInfo, "userInfouserInfo");
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
   * @description: 邀请有礼
   * @return {*}
   * @author: jinglin.gao
   */

  const inviteUser = () => {
    inviteGiftUrlInfoRef.current.getPage();
    hidePage();
  };

  /**
   * @description: 获取用户信息
   * @return {*}
   * @author: jinglin.gao
   */

  const getMemberUserInfoFn = async () => {
    try {
      let res = await getMemberUserInfo();
      if (res.code === 200) {
        setMemberUserInfo(res.result);
      }
    } catch (error) {
      console.log(error);
    }
  };

  /**
   * @description: 修改密码
   * @return {*}
   * @author: jinglin.gao
   */

  const changePassword = () => {
    hidePage();
    changePwdRef.current.getPage(1);
  };

  /**
   * @description: 反馈与建议
   * @return {*}
   * @author: jinglin.gao
   */
  const proposeFn = () => {
    proposeRef.current.getPage();
  };

  return (
    <>
      {pageState ? (
        <div className={styles.user_detail_mask}>
          <div className="user_detail-warp animate__animated animate__fadeInDown">
            <div className="user_detail-head">
              <span className="title">个人信息</span>
              <CloseOutlined
                onClick={hidePage}
                className="closeBtn"
                twoToneColor="#fff"
              ></CloseOutlined>
            </div>
            <div className="user_detail-content">
              <img
                className="user_avatar"
                src={userInfo?.headImgUrl || userDeafultImg}
                alt=""
              />
              <p className="detail_info">{userInfo?.nikeName}</p>

              <div className="invite_btn">
                <Button onClick={inviteUser} type="primary">
                  邀请有礼
                </Button>

                <Button
                  style={{ marginLeft: 10 }}
                  onClick={proposeFn}
                  type="primary"
                >
                  反馈与建议
                </Button>
              </div>

              <div className="accent_info">
                <ul className="accent_info_item">
                  <li>剩余时间</li>
                  <li>总次数</li>
                  <li>剩余次数</li>
                  <li style={{ width: "55%" }}>注册时间</li>
                </ul>
                <ul className="accent_info_content">
                  <li>{memberUserInfo?.deadline}天</li>
                  <li>{memberUserInfo?.totalCount}次</li>
                  <li>{memberUserInfo?.surplusCount}次</li>
                  <li style={{ width: "55%" }}>{userInfo?.createTime}</li>
                </ul>
              </div>

              <div className="user_detail_bottom">
                <span onClick={backLogin} className="tolls_btn">
                  退出登录
                </span>

                <span onClick={changePassword} className="tolls_btn">
                  修改密码
                </span>
              </div>
            </div>
          </div>
        </div>
      ) : (
        ""
      )}

      {/* 邀请有礼 */}
      <InviteGiftUrlInfo ref={inviteGiftUrlInfoRef}></InviteGiftUrlInfo>

      {/* 修改密码 */}
      <ChangePwd ref={changePwdRef}></ChangePwd>

      {/* 反馈与建议 */}
      <Propose ref={proposeRef}></Propose>
    </>
  );
});

export default UserDetail;
