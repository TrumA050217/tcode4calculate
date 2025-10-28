create table bank
(
    bank_id    bigint auto_increment comment '题库id'
        primary key,
    created_at datetime default CURRENT_TIMESTAMP null comment '创建时间'
)
    comment '题库';

create table my_result
(
    result_id bigint auto_increment comment '作答id'
        primary key,
    bank_id   bigint not null comment '题库id',
    total     int    not null comment '题目总数',
    accuracy  double not null comment '正确率',
    constraint my_result_bank_bank_id_fk
        foreign key (bank_id) references bank (bank_id)
)
    comment '用户作答表';

create table record
(
    record_id      bigint auto_increment comment '记录id'
        primary key,
    bank_id        bigint not null comment '关联题库id',
    operand_a      int    not null comment '操作数A',
    operand_b      int    not null comment '操作数B',
    type           int    not null comment '题目类型',
    correct_answer double not null comment '正确答案',
    constraint record_bank_bank_id_fk
        foreign key (bank_id) references bank (bank_id)
)
    comment '做题记录';

create table answer
(
    answer_id  bigint auto_increment comment '答案id'
        primary key,
    bank_id    bigint not null comment '题库id',
    record_id  bigint not null comment '题目id',
    my_answer  double null comment '我的答案',
    is_correct int    not null comment '是否是正确答案？1正确 2错误',
    constraint answer_bank_bank_id_fk
        foreign key (bank_id) references bank (bank_id),
    constraint answer_record_record_id_fk
        foreign key (record_id) references record (record_id)
)
    comment '用户答案';

create table mistake
(
    mistake_id   bigint auto_increment comment '错题id'
        primary key,
    bank_id      bigint not null comment '题库id',
    record_id    bigint not null comment '错题id',
    oper_a       int    not null comment '操作数A',
    oper_b       int    not null comment '操作数B',
    mistake_type int    not null comment '错题类型',
    constraint mistake_bank_bank_id_fk
        foreign key (bank_id) references bank (bank_id),
    constraint mistake_record_record_id_fk
        foreign key (record_id) references record (record_id)
)
    comment '错题本';

create table user
(
    user_id  bigint auto_increment comment '用户id'
        primary key,
    username varchar(255) not null comment '用户名',
    password varchar(255) not null comment '密码'
);

create table wx_user
(
    wx_user_id bigint auto_increment comment '微信用户id'
        primary key,
    openid     varchar(255) not null comment '微信用户openid',
    constraint wx_user_pk
        unique (openid)
)
    comment '微信登录用户';

