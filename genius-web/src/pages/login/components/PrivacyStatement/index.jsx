/*
 * @Description:
 * @Version: 2.0
 * @Autor: jinglin.gao
 * @Date: 2023-04-08 10:06:25
 * @LastEditors: jinglin.gao
 * @LastEditTime: 2023-05-01 23:44:13
 */
import React, { useState, forwardRef, useImperativeHandle } from "react";

import styles from "./index.module.less";
import { CloseOutlined } from "@ant-design/icons";
const PrivacyStatement = forwardRef((props, ref) => {
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
              <span className="title">免责声明</span>
              <CloseOutlined
                onClick={hidePage}
                className="closeBtn"
                twoToneColor="#fff"
              ></CloseOutlined>
            </div>

            <div className="custom_dialog-content">
              <p>
                在使用本网站提供的AI
                服务前，请您务必仔细阅读并理解本《免责声明》（以下简称“本声明”）。请您知悉，如果您选择继续访问本网站、或使用本网站提供的本服务以及通过各类方式利用本网站的行为（以下统称“本服务”），则视为接受并同意本声明全部内容。
                服务仅供个人学习、学术研究目的使用，未经许可，请勿分享、传播输入及生成的文本、图片内容。您在从事与本服务相关的所有行为(包括但不限于访问浏览、利用、转载、宣传介绍)时，必须以善意且谨慎的态度行事；您确保不得利用本服务故意或者过失的从事危害国家安全和社会公共利益、扰乱经济秩序和社会秩序、侵犯他人合法权益等法律、行政法规禁止的活动，并确保自定义输入文本不包含以下违反法律法规、政治相关、侵害他人合法权益的内容：
              </p>
              <p>
                1.反对宪法所确定的基本原则的；危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的；
              </p>
              <p>2.损害国家荣誉和利益的；</p>
              <p>
                3.歪曲、丑化、亵渎、否定英雄烈士事迹和精神，以侮辱、诽谤或者其他方式侵害英雄烈士的姓名、肖像、名誉、荣誉的；
              </p>

              <p>
                4.宣扬恐怖主义、极端主义或者煽动实施恐怖活动、极端主义活动的；
              </p>

              <p>5.煽动民族仇恨、民族歧视，破坏民族团结的；</p>

              <p>6.破坏国家宗教政策，宣扬邪教和封建迷信的；</p>

              <p>7.散布谣言，扰乱经济秩序和社会秩序的；</p>

              <p>8.散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的；</p>

              <p>9.侮辱或者诽谤他人，侵害他人名誉、隐私和其他合法权益的；</p>
              <p>
                10.含有虚假、有害、胁迫、侵害他人隐私、骚扰、侵害、中伤、粗俗、猥亵、或其它道德上令人反感的内容
              </p>
              <p>
                11.中国法律、法规、规章、条例以及任何具有法律效力之规范所限制或禁止的其它内容。
              </p>

              <p>
                12.您确认使用本服务时输入的内容将不包含您的个人信息。您同意并承诺，在使用本服务时，不会披露任何保密、敏感或个人信息。
              </p>

              <p>
                13.您确认并知悉本服务生成的所有内容都是由人工智能模型生成，所以可能会出现意外和错误的情况，请确保检查事实。我们对其生成内容的准确性、完整性和功能性不做任何保证，并且其生成的内容不代表我们的态度或观点，仅为提供更多信息，也不构成任何建议或承诺。对于您根据本服务提供的信息所做出的一切行为，除非另有明确的书面承诺文件，否则我们不承担任何形式的责任。
              </p>

              <p>
                14.本服务来自于法律法规允许的包括但不限于公开互联网等信息积累，因互联网的开放性属性，不排除其中部分信息具有瑕疵、不合理或引发不快。遇有此情形的，欢迎并感谢您随时通过留言举报。
              </p>

              <p>
                15.不论在何种情况下，本网站均不对由于网络连接故障，电力故障，罢工，劳动争议，暴乱，起义，骚乱，火灾，洪水，风暴，爆炸，不可抗力，战争，政府行为，国际、国内法院的命令，黑客攻击，互联网病毒，网络运营商技术调整，政府临时管制或任何其他不能合理控制的原因而造成的本服务不能访问、服务中断、信息及数据的延误、停滞或错误，不能提供或延迟提供服务而承担责任。
              </p>

              <p>
                16.当本服务以链接形式推荐其他网站内容时，我们并不对这些网站或资源的可用性负责，且不保证从这些网站获取的任何内容、产品、服务或其他材料的真实性、合法性。在法律允许的范围内，本网站不承担您就使用本服务所提供的信息或任何链接所引致的任何直接、间接、附带、从属、特殊、继发、惩罚性或惩戒性的损害赔偿。
              </p>

              <p>17.本产品包含会员服务为虚拟产品，支付成功后不支持退款</p>
            </div>
          </div>
        </div>
      ) : (
        ""
      )}
    </>
  );
});

export default PrivacyStatement;
