/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-08 10:06:25
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-27 10:39:45
 */
import React, { useState, forwardRef, useImperativeHandle } from "react";

import styles from "./index.module.less";
import { CloseOutlined } from "@ant-design/icons";
import officialAccountImg from "../../../../../public/assets/imgs/officialAccountImg.jpg";
import copy from "../../../../../public/assets/imgs/copy.png";
import SharkBtn from "@/components/SharkBtn";
import { copyToClipboardFn } from "@/utils";
import { useSelector } from "react-redux";
const FreeQuestion = forwardRef((props, ref) => {
  const [pageState, setPageState] = useState(false);
  const userInfo = useSelector((state) => state.userInfo);

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
  const getPage = () => {
    setPageState(true);
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
   * @description: 一键复制回复内容
   * @return {*}
   * @author: jinglin.gao
   */
  /**
   * @description: 复制会话内容
   * @return {*}
   * @author: jinglin.gao
   */
  const copyToClipboard = (e, data) => {
    e.stopPropagation();
    let copyData = "领取ChatGPT次数:" + userInfo.nikeName;
    copyToClipboardFn(copyData, "复制内容成功");
  };

  return (
    <>
      {pageState ? (
        <div className={styles.custom_dialog}>
          <div className="custom_dialog-warp">
            <div className="custom_dialog-head">
              <span className="title">免费领取次数</span>
              <CloseOutlined
                onClick={hidePage}
                className="closeBtn"
                twoToneColor="#fff"
              ></CloseOutlined>
            </div>

            <div className="custom_dialog-content">
              <div className="free_question-info">
                扫码关注公众号，回复”领取ChatGPT次数:(您的账号)“，即可获得免费次数
                <p className="demoInfo">
                  例如:领取ChatGPT次数:GeniusAi@gmail.com
                </p>
              </div>
              <div className="copyBtn">
                <SharkBtn
                  onClick={copyToClipboard}
                  icon={copy}
                  name="一键复制回复内容"
                ></SharkBtn>
              </div>

              <img
                className="free_question-img"
                src={officialAccountImg}
                alt=""
              />
            </div>
          </div>
        </div>
      ) : (
        ""
      )}
    </>
  );
});

export default FreeQuestion;
