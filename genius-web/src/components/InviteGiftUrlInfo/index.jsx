/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-06 08:57:51
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-19 09:23:33
 */
import React, { useState, forwardRef, useImperativeHandle } from "react";
import styles from "./index.module.less";
import { CloseOutlined } from "@ant-design/icons";
import { Input, Button } from "antd";
import { inviteGiftUrlInfo } from "@/api/user";
import { copyToClipboardFn } from "@/utils";
const InviteGiftUrlInfo = forwardRef((props, ref) => {
  const [pageState, setPageState] = useState(false);
  const [inviteUrl, setInviteUrl] = useState("");
  const [inviteDesc, setInviteDesc] = useState("");
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
    inviteGiftUrlInfoFn();
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
   * @description: 一键复制
   * @return {*}
   * @author: jinglin.gao
   */
  const copyToClipboard = () => {
    copyToClipboardFn(inviteUrl, "邀请链接复制成功,快去分享吧");
  };

  /**
   * @description: 获取分享链接
   * @return {*}
   * @author: jinglin.gao
   */

  const inviteGiftUrlInfoFn = async () => {
    try {
      let res = await inviteGiftUrlInfo();
      if (res.code === 200) {
        const { code, url, desc } = res.result;
        let inviteUrl = `${url}/#/login?code=${code}`;
        setInviteUrl(inviteUrl);

        const tranformDesc =
          desc.indexOf("-1") !== -1 ? desc.replace("-1天", "无限期") : desc;

        console.log(tranformDesc, "tranformDesc");
        setInviteDesc(tranformDesc);
      }
    } catch (error) {
      console.log(error);
    }
  };
  return pageState ? (
    <div className={styles.invite_giftUrl_info}>
      <div className="user_detail-warp">
        <div className="user_detail-head">
          <span className="title">邀请有礼</span>
          <CloseOutlined
            onClick={hidePage}
            className="closeBtn"
            twoToneColor="#fff"
          ></CloseOutlined>
        </div>
        <div className="user_detail-content">
          <p className="title">说明</p>
          <div className="content">{inviteDesc}</div>
          <p className="invite_url_title">您的专属邀请链接</p>

          <div className="invite_url_warp">
            <Input.Group compact>
              <Input
                readOnly
                style={{ width: "calc(100% - 200px)" }}
                value={inviteUrl}
              />
              <Button onClick={copyToClipboard} type="primary">
                一键复制
              </Button>
            </Input.Group>
          </div>

          <div className="mobile_invite_url_warp">
            <Input.Group compact>
              <Input readOnly value={inviteUrl} />
            </Input.Group>

            <Button className="copybtn" onClick={copyToClipboard} type="primary">
              一键复制
            </Button>
          </div>
        </div>
      </div>
    </div>
  ) : (
    ""
  );
});

export default InviteGiftUrlInfo;
