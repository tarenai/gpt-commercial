/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-08 10:06:25
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-10 09:02:03
 */
import React, { useState, forwardRef, useImperativeHandle } from "react";
import { Input, Button, message } from "antd";
import _ from "lodash";
import styles from "./index.module.less";
import { CloseOutlined } from "@ant-design/icons";
import { sendFeedback } from "@/api/user";
const Propose = forwardRef((props, ref) => {
  const [pageState, setPageState] = useState(false);
  const [proposeContent, setProposeContent] = useState("");
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
    setProposeContent("");
  };

  /**
   * @description: 弹框隐藏
   * @return {*}
   * @author: jinglin.gao
   */

  const hidePage = () => {
    setPageState(false);
  };

  const changeProposeContent = (e) => {
    setProposeContent(e.target.value);
  };

  /**
   * @description: 提交意见
   * @return {*}
   * @author: jinglin.gao
   */
  const submit = async () => {
    if (!proposeContent) {
      message.error("请输入反馈内容");
      return;
    }
    try {
      let data = {
        desc: proposeContent,
      };
      let res = await sendFeedback(data);
      if (res.code === 200) {
        hidePage();
        message.success("反馈成功，感谢您的建议");
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <>
      {pageState ? (
        <div className={styles.custom_dialog}>
          <div className="custom_dialog-warp">
            <div className="custom_dialog-head">
              <span className="title">反馈与建议</span>
              <CloseOutlined
                onClick={hidePage}
                className="closeBtn"
                twoToneColor="#fff"
              ></CloseOutlined>
            </div>

            <div className="custom_dialog-content">
              <Input.TextArea
                onChange={changeProposeContent}
                value={proposeContent}
                rows={4}
                placeholder="请输入您的建议与反馈"
              />

              <div className="user_detail_bottom">
                <Button
                  onClick={_.debounce(submit, 500)}
                  className="tolls_btn"
                >
                  提交
                </Button>
              </div>
            </div>
          </div>
        </div>
      ) : (
        ""
      )}
    </>
  );
});

export default Propose;
