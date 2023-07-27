/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-08 10:06:25
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-21 15:36:36
 */
import React, { useState, forwardRef, useImperativeHandle } from "react";
import styles from "./index.module.less";
import { CloseOutlined } from "@ant-design/icons";
import { Tabs } from "antd";
import qqGroup from "../../../../../public/assets/imgs/qqGroup.png";
import wxStar from "../../../../../public/assets/imgs/wxStar.jpg";
const JoinGroup = forwardRef((props, ref) => {
  const [pageState, setPageState] = useState(false);
  const [pageData, setPageData] = useState({});
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
  const getPage = (data) => {
    setPageState(true);
    setPageData(data);
  };

  /**
   * @description: 弹框隐藏
   * @return {*}
   * @author: jinglin.gao
   */

  const hidePage = () => {
    setPageState(false);
  };

  return (
    <>
      {pageState ? (
        <div className={styles.custom_dialog}>
          <div className="custom_dialog-warp animate__animated animate__fadeInDown">
            <div className="custom_dialog-head">
              <span className="title">{pageData.buttonName}</span>
              <CloseOutlined
                onClick={hidePage}
                className="closeBtn"
                twoToneColor="#fff"
              ></CloseOutlined>
            </div>

            <div className="custom_dialog-content">
              <p className="jonUstitle">{pageData.buttonDesc}</p>
              <img className="groupImg" src={pageData.imageUrl} alt="" />
            </div>
          </div>
        </div>
      ) : (
        ""
      )}
    </>
  );
});

export default JoinGroup;
