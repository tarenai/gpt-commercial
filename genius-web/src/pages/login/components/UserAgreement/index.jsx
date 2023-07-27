/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-08 10:06:25
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-04-09 10:55:44
 */
import React, {
  useState,
  forwardRef,
  useImperativeHandle,
  useRef,
} from "react";

import styles from "./index.module.less";
import { CloseOutlined } from "@ant-design/icons";
const UserAgreement = forwardRef((props, ref) => {
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
              <span className="title">用户协议</span>
              <CloseOutlined
                onClick={hidePage}
                className="closeBtn"
                twoToneColor="#fff"
              ></CloseOutlined>
            </div>

            <div className="custom_dialog-content">
              <div className="typora-export-content">
                <div id="write">
                  <p>
                    <span>
                      欢迎使用AI问答平台（以下简称“本平台”）。在使用本平台之前，请您务必认真阅读本用户协议（以下简称“协议”）。
                    </span>
                  </p>
                  <p>
                    <span>1.用户账户</span>
                    <span>1.1 用户注册</span>
                  </p>
                  <p>
                    <span>
                      用户需要提供真实、准确、完整的个人信息注册本平台账户。用户需要确保所提供的个人信息在注册后能够及时更新。我们有权利根据实际情况对用户所提供的信息进行审核，并要求用户进行必要的补充、更新、更正和改变。
                    </span>
                  </p>
                  <p>
                    <span>1.2 用户账户安全</span>
                  </p>
                  <p>
                    <span>
                      用户须对其账户和密码保管妥当，并对以其账户进行的所有活动负全责。如用户发现任何未经授权使用其账户的情况，应立即通知我们。
                    </span>
                  </p>
                  <p>
                    <span>1.3 用户行为规范</span>
                  </p>
                  <p>
                    <span>
                      用户在使用本平台的过程中，应遵守中国的相关法律法规和本协议的约定。用户不得利用本平台从事以下行为：
                    </span>
                  </p>
                  <p>
                    <span>
                      （1）发布、传送、传播违法、有害、恶意、骚扰、侮辱、诽谤、淫秽、猥亵、威胁、侵犯他人隐私或其他违反社会公共利益或公共道德的信息；
                    </span>
                  </p>
                  <p>
                    <span>
                      （2）对其他用户进行欺骗、恶意评价或其他不正当的竞争行为；
                    </span>
                  </p>
                  <p>
                    <span>
                      （3）利用本平台进行任何可能对互联网的正常运转造成不利影响的行为；
                    </span>
                  </p>
                  <p>
                    <span>
                      （4）侵犯本平台或其他任何第三方的知识产权或其他合法权益；
                    </span>
                  </p>
                  <p>
                    <span>（5）其他违反法律法规、本协议或公序良俗的行为。</span>
                  </p>
                  <p>
                    <span>2.平台服务</span>
                    <span>2.1 服务内容</span>
                  </p>
                  <p>
                    <span>
                      本平台提供的服务包括但不限于：提供基于人工智能技术的问答服务，提供搜索服务，提供与其他用户的交互服务等。我们有权根据实际情况对服务内容进行变更，并在本平台上公告。
                    </span>
                  </p>
                  <p>
                    <span>2.2 服务的限制</span>
                  </p>
                  <p>
                    <span>
                      本平台将尽力维护服务的及时性和安全性，但无法保证服务不会受到干扰、中断或故障。对于因不可抗力或第三方原因导致的服务中断或停止，我们不承担任何责任。
                    </span>
                  </p>
                  <p>
                    <span>2.3 服务费用</span>
                  </p>
                  <p>
                    <span>
                      目前本平台服务是免费体验的，但我们有权随时根据实际情况对服务收费。如有变动，我们将在本平台上进行公告或通知。
                    </span>
                  </p>
                  <p>
                    <span>3.用户隐私</span>
                    <span>
                      我们将严格遵守相关法律法规和隐私政策，保护用户的隐私信息。我们将采取合理措施确保用户的个人信息安全，不会向第三方透露用户的个人信息，除非得到用户的同意或法律法规规定或行政、司法机关的要求。
                    </span>
                  </p>
                  <p>
                    <span>4.用户知识产权</span>
                    <span>
                      用户在使用本平台的过程中所上传、发布、传送、共享的任何内容（包括但不限于文字、图片、音频、视频等），均视为用户授予我们非独占、全球范围内、永久性、可转让的使用权利，我们有权将该内容用于平台服务的展示、推广、分析等方面。
                    </span>
                  </p>
                  <p>
                    <span>5.免责声明</span>
                    <span>
                      本平台不对用户上传的内容进行审查、筛选和修改，不对该内容的真实性、准确性、完整性和可靠性承担任何责任。本平台不对用户在使用本平台过程中遭受的任何损失或损害承担任何责任，包括但不限于直接或间接的损失或损害、利润损失、商业机会损失等。
                    </span>
                  </p>
                  <p>
                    <span>6.协议变更和解释</span>
                    <span>
                      我们有权随时根据需要修改本协议，并在本平台上进行公告。如用户不同意本协议的修改，可以选择停止使用本平台的服务。用户继续使用本平台服务即视为同意接受修改后的协议。本协议的解释权归我们所有。
                    </span>
                  </p>
                  <p>
                    <span>7.法律适用和争议解决</span>
                    <span>
                      本协议适用中华人民共和国法律。因本协议引起的或与本协议有关的任何争议，应尽量通过友好协商解决。协商不成的，用户和我们均可以将争议提交到上海市有管辖权的人民法院解决。
                    </span>
                  </p>
                  <p>
                    <span>8.其他</span>
                    <span>
                      本协议构成用户与我们之间的完整协议，取代用户和我们之间的任何其他先前的协议或约定。如本协议中的任何条款无法执行或被判定为无效，则该条款将被视为被剔除，而其他条款将继续有效。
                    </span>
                  </p>
                </div>
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

export default UserAgreement;
