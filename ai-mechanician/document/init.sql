create table account
(
    id              bigint unsigned auto_increment comment '主键'
        primary key,
    user_id         bigint                                not null comment '用户ID',
    account_balance decimal(10, 2)                        not null comment '账户余额',
    create_by       varchar(64) default ''                not null comment '创建人',
    create_time     datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by       varchar(64) default ''                not null comment '修改人',
    update_time     datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    yn              tinyint     default 1                 not null comment '0无效1有效'
)
    comment '账户表';

create index idx_user_id
    on account (user_id);

create table account_log
(
    id                   bigint unsigned auto_increment comment '主键'
        primary key,
    user_id              bigint                                not null comment '用户ID',
    amount               decimal(10, 2)                        not null comment '变动金额',
    balance_amount       decimal(10, 2)                        not null comment '变动后余额',
    request_id           varchar(54) default ''                not null comment '请求ID',
    outside_code         varchar(54) default ''                not null comment '业务单号',
    log_description      varchar(54)                           not null comment '描述',
    log_description_type tinyint                               not null comment '1: 充值 2: 失效回收 3: 退款 4: 消费',
    direction_type       tinyint     default 10                not null comment '类型 10: 转入 20:转出',
    create_by            varchar(64) default ''                not null comment '创建人',
    create_time          datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by            varchar(64) default ''                not null comment '修改人',
    update_time          datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    yn                   tinyint     default 1                 not null comment '0无效1有效'
)
    comment '账户表日志';

create index idx_user_id
    on account_log (user_id);

create table advertise_config
(
    id             bigint unsigned auto_increment comment '主键'
        primary key,
    advertise_name varchar(64)  default ''                not null comment '广告名称',
    advertise_link varchar(500) default ''                not null comment '广告链接',
    img_link       varchar(500) default ''                not null comment '图片链接',
    advertise_type varchar(20)  default ''                not null comment '广告类型 10: 加入我们(第一位) 11:加入我们(第二位) 20:右侧广告位列表',
    create_by      varchar(64)  default ''                not null comment '创建人',
    create_time    datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by      varchar(64)  default ''                not null comment '修改人',
    update_time    datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    yn             tinyint      default 1                 not null comment '0无效1有效'
)
    comment '广告配置表';

create table chat_detail_log
(
    id          bigint unsigned auto_increment comment '主键'
        primary key,
    user_id     bigint                                not null comment '用户ID',
    request_id  varchar(64) default ''                not null comment '请求id',
    chat_role   varchar(54) default ''                not null comment '角色',
    content     text                                  not null comment '信息',
    create_by   varchar(64) default ''                not null comment '创建人',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by   varchar(64) default ''                not null comment '修改人',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    yn          tinyint     default 1                 not null comment '0无效1有效'
)
    comment '问答记录表';

create index idx_request_id
    on chat_detail_log (request_id);

create index idx_user_id
    on chat_detail_log (user_id);

create table chat_gpt_config
(
    id          bigint unsigned auto_increment comment '主键'
        primary key,
    api_key     varchar(200) default ''                not null comment 'apiKey',
    create_by   varchar(64)  default ''                not null comment '创建人',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by   varchar(64)  default ''                not null comment '修改人',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    yn          tinyint      default 1                 not null comment '0无效1有效'
)
    comment '用户信息表';

create table exchange_card_detail
(
    id             bigint unsigned auto_increment comment '主键'
        primary key,
    user_id        bigint                                not null comment '用户ID',
    member_card_id bigint                                not null comment '会员卡ID',
    total_count    int         default 0                 not null comment '总次数',
    surplus_count  int         default 0                 not null comment '剩余次数',
    expires_time   datetime                              not null comment '失效时间',
    exchange_state tinyint                               not null comment '状态 1:已兑换 2:过期',
    create_by      varchar(64) default ''                not null comment '创建人',
    create_time    datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by      varchar(64) default ''                not null comment '修改人',
    update_time    datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    yn             tinyint     default 1                 not null comment '0无效1有效'
)
    comment '兑换卡详情';

create index idx_expires_time
    on exchange_card_detail (expires_time);

create index idx_member_card_id
    on exchange_card_detail (member_card_id);

create index idx_user_id
    on exchange_card_detail (user_id);

create table feedback
(
    id          bigint unsigned auto_increment comment '主键'
        primary key,
    user_id     bigint                                 not null comment '用户ID',
    proposals   varchar(500) default ''                not null comment '建议',
    create_by   varchar(64)  default ''                not null comment '创建人',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by   varchar(64)  default ''                not null comment '修改人',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    yn          tinyint      default 1                 not null comment '0无效1有效'
)
    comment '用户反馈表';

create index idx_f_user_id
    on feedback (user_id);

create table invite_log
(
    id                bigint unsigned auto_increment comment '主键'
        primary key,
    invite_user_id    bigint                                not null comment '邀请人用户ID',
    to_invite_user_id bigint                                not null comment '被邀请人用户ID',
    product_info_id   bigint                                not null comment '商品ID',
    create_by         varchar(64) default ''                not null comment '创建人',
    create_time       datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by         varchar(64) default ''                not null comment '修改人',
    update_time       datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    yn                tinyint     default 1                 not null comment '0无效1有效'
)
    comment '邀请记录';

create index idx_invite_user_id
    on invite_log (invite_user_id);

create index idx_to_invite_user_id
    on invite_log (to_invite_user_id);

create table member_card
(
    id          bigint unsigned auto_increment comment '主键'
        primary key,
    card_code   varchar(64)                           not null comment '会员卡编码',
    card_name   varchar(64)                           not null comment '会员卡名',
    card_type   tinyint                               not null comment '卡类型 1: 普通类型 2: 拉新赠送',
    card_state  tinyint     default 1                 not null comment '卡状态',
    card_sort   int         default 1                 not null comment '排序',
    view_type   tinyint     default 1                 not null comment '显示类型 1: 前端 2: 后台',
    amount      decimal(10, 2)                        not null comment '卡金额',
    card_day    int                                   not null comment '卡天数',
    create_by   varchar(64) default ''                not null comment '创建人',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by   varchar(64) default ''                not null comment '修改人',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    yn          tinyint     default 1                 not null comment '0无效1有效'
)
    comment '会员卡';

create index idx_card_code
    on member_card (card_code);

create table member_rights
(
    id          bigint unsigned auto_increment comment '主键'
        primary key,
    rights_name varchar(64) default ''                not null comment '策略名称',
    member_code varchar(64) default ''                not null comment '会员编码',
    count       int(10)     default 0                 not null comment '次数',
    create_by   varchar(64) default ''                not null comment '创建人',
    create_time datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by   varchar(64) default ''                not null comment '修改人',
    update_time datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    yn          tinyint     default 1                 not null comment '0无效1有效'
)
    comment '会员策略';

create index idx_member_code
    on member_rights (member_code);

create table payment_info
(
    id           bigint unsigned auto_increment comment '主键'
        primary key,
    user_id      bigint                                   not null comment '用户ID',
    pay_sn       varchar(64)    default ''                not null comment '支付流水',
    goods_code   varchar(64)    default ''                not null comment '商品编码',
    out_pay_sn   varchar(64)    default ''                not null comment '三方支付流水',
    pay_amount   decimal(10, 2) default 0.00              not null comment '支付金额',
    pay_state    int            default 0                 not null comment '支付状态 0: 支付中 1: 已支付 2: 支付失败 3: 支付取消',
    pay_type     int                                      not null comment '支付类型 10:微信 20:支付宝',
    pay_merchant varchar(64)    default ''                not null comment '商户号',
    pay_time     datetime                                 null comment '支付时间',
    create_by    varchar(64)    default ''                not null comment '创建人',
    create_time  datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by    varchar(64)    default ''                not null comment '修改人',
    update_time  datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    yn           tinyint        default 1                 not null comment '0无效1有效'
)
    comment '用户信息表';

create index idx_pay_sn
    on payment_info (pay_sn);

create index idx_user_id
    on payment_info (user_id);

create table top_bar_config
(
    id          bigint unsigned auto_increment comment '主键'
        primary key,
    button_name varchar(64)  default ''                not null comment '按钮名称',
    button_desc varchar(500) default ''                not null comment '按钮描述',
    button_type int(2)       default 10                not null comment '10:弹窗 20:跳转',
    jump_url    varchar(500) default ''                not null comment '跳转url',
    image_url   varchar(500) default ''                not null comment '图片',
    create_by   varchar(64)  default 'system'          not null comment '创建人',
    create_time datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by   varchar(64)  default 'system'          not null comment '修改人',
    update_time datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    yn          tinyint      default 1                 not null comment '0无效1有效'
)
    comment '上拦配置表';

create table user_info
(
    id                bigint unsigned auto_increment comment '主键'
        primary key,
    phone             varchar(13) default ''                not null comment '手机号',
    email             varchar(64) default ''                not null comment '邮箱',
    user_password     varchar(64) default ''                not null comment '密码',
    user_status       tinyint     default 1                 not null comment '状态 1: 正常 2: 注销 3: 冻结',
    member_valid_time datetime                              null comment '会员有效日期',
    register_type     tinyint(1)  default 1                 not null comment '1: 邮箱 2: 短信 3: 公众号授权登录',
    create_by         varchar(64) default ''                not null comment '创建人',
    create_time       datetime    default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by         varchar(64) default ''                not null comment '修改人',
    update_time       datetime    default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    yn                tinyint     default 1                 not null comment '0无效1有效'
)
    comment '用户信息表';

create index idx_email
    on user_info (email);

create index idx_phone
    on user_info (phone);

create table wechat_user_info
(
    id           bigint unsigned auto_increment comment '主键'
        primary key,
    user_id      bigint                                 not null comment '用户ID',
    open_id      varchar(64)  default ''                not null comment '信息',
    unionId      varchar(64)  default ''                not null comment '用户统一标识',
    nick_name    varchar(64)  default ''                not null comment '普通用户昵称',
    city         varchar(10)  default ''                not null comment '普通用户个人资料填写的城市',
    province     varchar(10)  default ''                not null comment '普通用户个人资料填写的省份',
    country      varchar(10)  default ''                not null comment '国家',
    headImgUrl   varchar(500) default ''                not null comment '头像',
    sex          tinyint                                null comment '普通用户性别，1为男性，2为女性',
    snapshotUser tinyint                                null comment '是否为快照页模式虚拟账号，值为0时是普通用户，1时是虚拟帐号',
    create_by    varchar(64)  default ''                not null comment '创建人',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_by    varchar(64)  default ''                not null comment '修改人',
    update_time  datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    yn           tinyint      default 1                 not null comment '0无效1有效'
)
    comment '用户信息表';

create index idx_open_id
    on wechat_user_info (open_id);

create index idx_user_id
    on wechat_user_info (user_id);


INSERT INTO ai_mechanician.member_card (id, card_code, card_name, card_type, card_state, card_sort, view_type, amount, card_day, create_by, create_time, update_by, update_time, yn) VALUES (1, 'day_1', '日卡', 1, 1, 1, 1, 1.00, 1, 'system', now(), 'system', now(), 1);
INSERT INTO ai_mechanician.member_card (id, card_code, card_name, card_type, card_state, card_sort, view_type, amount, card_day, create_by, create_time, update_by, update_time, yn) VALUES (2, 'month_1', '月卡', 1, 1, 2, 1, 19.00, 30, 'system', now(), 'system', now(), 1);
INSERT INTO ai_mechanician.member_card (id, card_code, card_name, card_type, card_state, card_sort, view_type, amount, card_day, create_by, create_time, update_by, update_time, yn) VALUES (3, 'season_1', '季卡', 1, 1, 3, 1, 59.00, 90, 'system', now(), 'system', now(), 1);
INSERT INTO ai_mechanician.member_card (id, card_code, card_name, card_type, card_state, card_sort, view_type, amount, card_day, create_by, create_time, update_by, update_time, yn) VALUES (5, 'invite01', '拉新', 2, 1, 4, 2, 0.00, 30, 'system', now(), 'system', now(), 1);
INSERT INTO ai_mechanician.member_card (id, card_code, card_name, card_type, card_state, card_sort, view_type, amount, card_day, create_by, create_time, update_by, update_time, yn) VALUES (6, 'register01', '注册有礼', 2, 1, 5, 2, 0.00, 2, 'system', now(), 'system', now(), 1);
INSERT INTO ai_mechanician.member_card (id, card_code, card_name, card_type, card_state, card_sort, view_type, amount, card_day, create_by, create_time, update_by, update_time, yn) VALUES (7, 'mp_give01', '公众号领取', 2, 1, 6, 2, 0.00, 1, 'system', now(), 'system', now(), 1);


INSERT INTO ai_mechanician.member_rights (id, rights_name, member_code, count, create_by, create_time, update_by, update_time, yn) VALUES (1, '日权益', 'day_1', 30, 'system', now(), 'system', now(), 1);
INSERT INTO ai_mechanician.member_rights (id, rights_name, member_code, count, create_by, create_time, update_by, update_time, yn) VALUES (2, '月权益', 'month_1', 2000, 'system', now(), 'system', now(), 1);
INSERT INTO ai_mechanician.member_rights (id, rights_name, member_code, count, create_by, create_time, update_by, update_time, yn) VALUES (3, '季权益', 'season_1', 99999, 'system', now(), 'system', now(), 1);
INSERT INTO ai_mechanician.member_rights (id, rights_name, member_code, count, create_by, create_time, update_by, update_time, yn) VALUES (4, '邀请有礼', 'invite01', 20, 'system', now(), 'system', now(), 1);
INSERT INTO ai_mechanician.member_rights (id, rights_name, member_code, count, create_by, create_time, update_by, update_time, yn) VALUES (5, '注册送权益', 'register01', 50, 'system', now(), 'system', now(), 1);
INSERT INTO ai_mechanician.member_rights (id, rights_name, member_code, count, create_by, create_time, update_by, update_time, yn) VALUES (6, '公众号领取', 'mp_give01', 5, 'system', now(), 'system', now(), 1);
