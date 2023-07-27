/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-08 10:06:25
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-21 09:00:33
 */
import React, { useState, forwardRef, useImperativeHandle } from "react";
import styles from "./index.module.less";
import { CloseOutlined } from "@ant-design/icons";
import { Tabs } from "antd";
import qqGroup from "../../../../../public/assets/imgs/qqGroup.png";
import wxStar from "../../../../../public/assets/imgs/wxStar.jpg";
const JoinGroup = forwardRef((props, ref) => {
  const [pageState, setPageState] = useState(false);

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

  return (
    <>
      {pageState ? (
        <div className={styles.custom_dialog}>
          <div className="custom_dialog-warp">
            <div className="custom_dialog-head">
              <span className="title">加入我们</span>
              <CloseOutlined
                onClick={hidePage}
                className="closeBtn"
                twoToneColor="#fff"
              ></CloseOutlined>
            </div>

            <div className="custom_dialog-content">
              <p className="jonUstitle">加入我们，一起学习更多Ai相关知识</p>
              <Tabs defaultActiveKey="1" centered>
                <Tabs.TabPane tab="Q群" key="1">
                  <p className="jonUstitle">Q群 452136010</p>
                  <img className="groupImg" src={qqGroup} alt="" />
                </Tabs.TabPane>
                <Tabs.TabPane tab="微信星球" key="2">
                  <img className="groupImg" src={wxStar} alt="" />
                </Tabs.TabPane>
              </Tabs>
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
