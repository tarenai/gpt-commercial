/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2022-08-18 13:59:22
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-07 17:23:36
 */
import React, { useRef, useEffect, useState } from "react";
import logo from "../../../public/assets/imgs/logo.svg";
import { Layout, Dropdown, Space, Menu } from "antd";
import { DownOutlined } from "@ant-design/icons";
import styles from "./index.module.less";
import UserDetail from "./components/UserDetail";
import { useHistory } from "react-router-dom";
import { getUserInfo, getNavList } from "@/api/user";
import { systemConfig } from "@/api/login";
import { userInfoAction } from "@/store/actions/home_action";
import { useDispatch } from "react-redux";
import InviteGiftUrlInfo from "@/components/InviteGiftUrlInfo";
import FreeQuestion from "./components/FreeQuestion";
import userDeafultImg from "../../../public/assets/imgs/userDeafultImg.svg";
import JoinGroup from "./components/JoinGroup";
const { Header } = Layout;

const HeadComponent = (props) => {
  const dispatch = useDispatch();
  const history = useHistory();
  const userDetailRef = useRef(null);
  const [userInfo, setUserInfo] = useState(null);
  const inviteGiftUrlInfoRef = useRef(null);
  const freeQuestionRef = useRef(null);
  const joinGroupRef = useRef(null);
  const [navList, setNavList] = useState([]); // 用户所在的级别列表，可以获取到用户所在
  // 系统配置
  const [sysConfig, setSysConfig] = useState({});

  useEffect(() => {
    getUserInfoFn();

    systemConfigFn();

    getNavListFn();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // 获取系统配置
  const systemConfigFn = async () => {
    try {
      let res = await systemConfig();
      if (res.code === 200) {
        let resData = res.result;
        setSysConfig(resData);

        // 设置网站图标
        var link =
          document.querySelector("link[rel*='icon']") ||
          document.createElement("link");

        link.type = "image/x-icon";

        link.rel = "shortcut icon";

        link.href = resData?.iconUrl || "";

        document.getElementsByTagName("head")[0].appendChild(link);
      }
    } catch (error) {
      console.log(error);
    }
  };

  /**
   * @description: 获取用户详情
   * @return {*}
   * @author: jinglin.gao
   */
  const getUserDetail = () => {
    userDetailRef.current.getPage();
  };

  /**
   * @description: 获取用户信息
   * @return {*}
   * @author: jinglin.gao
   */

  const getUserInfoFn = async () => {
    try {
      let res = await getUserInfo();
      if (res.code === 200) {
        let resData = res.result;
        setUserInfo(resData);
        dispatch(userInfoAction(resData));
      }
    } catch (error) {
      console.log(error);
    }
  };

  /**
   * @description: 购买vip
   * @return {*}
   * @author: jinglin.gao
   */
  const buyVip = () => {
    history.replace("/ai/commodity");
  };

  /**
   * @description: 免费领取次数
   * @return {*}
   * @author: jinglin.gao
   */

  const freeQuestion = () => {
    freeQuestionRef.current.getPage();
  };

  /**
   * @description: 加群
   * @return {*}
   * @author: jinglin.gao
   */

  const joinGroup = () => {
    joinGroupRef.current.getPage();
  };

  // 获取导航列表
  const getNavListFn = async () => {
    try {
      let res = await getNavList();
      if (res.code === 200) {
        setNavList(res.result || []);
      }
    } catch (error) {
      console.log(error);
    }
  };
  // 自定义弹窗
  const customDialog = (data) => {
    if (data.buttonType === 10) {
      joinGroupRef.current.getPage(data);
    } else {
      window.open(data.jumpUrl);
    }
  };
  return (
    <>
      <Header className={styles.header}>
        <div className="logoBox">
          <img className="logo" src={sysConfig?.iconUrl || logo} alt="" />
          <p className="title">{sysConfig.webName || "Genius"}</p>
        </div>

        <div className="userInfo">
          {navList.map((v) => (
            <div key={v.id} onClick={() => customDialog(v)} className="propose">
              {v.buttonName}
            </div>
          ))}

          {/* <div onClick={joinGroup} className="propose">
            加入我们
          </div>

          <div onClick={freeQuestion} className="freeQuestion">
            免费领取次数
          </div> */}

          <div onClick={buyVip} className="bug_vip">
            购买会员
          </div>

          {/* <Button size="small" shape="circle" icon={<BellOutlined />} /> */}

          <div className="user_info-box">
            <img
              className="user_avatar"
              src={userInfo?.headImgUrl || userDeafultImg}
              alt=""
            />
            <span onClick={getUserDetail} className="userName">
              {userInfo?.nikeName ||
                `游客 ${(Math.random() * 1000).toFixed(0)}`}
            </span>
          </div>
        </div>

        <div className="mobile_userInfo">
          <div className="mobile_userInfo-warp">
            {/* <div onClick={joinGroup} className="propose">
              加群
            </div>

            <div onClick={freeQuestion} className="freeQuestion">
              领取
            </div> */}

            <Dropdown
              className="propose"
              overlay={
                <Menu>
                  {navList.map((v) => (
                    <Menu.Item key={v.id} onClick={() => customDialog(v)}>
                      {v.buttonName}
                    </Menu.Item>
                  ))}
                </Menu>
              }
              trigger={["click"]}
            >
              <Space>
                <a
                  className="ant-dropdown-link"
                  onClick={(e) => e.preventDefault()}
                >
                  更多福利
                  <DownOutlined />
                </a>
              </Space>
            </Dropdown>

            <div onClick={buyVip} className="bug_vip">
              购买
            </div>

            {/* <Button size="small" shape="circle" icon={<BellOutlined />} /> */}

            <div className="user_info-box">
              <img
                onClick={getUserDetail}
                className="user_avatar"
                src={userInfo?.headImgUrl || userDeafultImg}
                alt=""
              />
            </div>
          </div>
        </div>
      </Header>

      {/* 用户信息 */}
      <UserDetail ref={userDetailRef}></UserDetail>

      {/* 邀请返利 */}
      <InviteGiftUrlInfo ref={inviteGiftUrlInfoRef}></InviteGiftUrlInfo>

      {/* 免费领取次数 */}
      <FreeQuestion ref={freeQuestionRef}></FreeQuestion>

      {/* 加入我们 */}
      <JoinGroup ref={joinGroupRef} />
    </>
  );
};

export default HeadComponent;
