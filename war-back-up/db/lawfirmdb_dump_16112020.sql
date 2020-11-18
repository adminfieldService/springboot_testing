--
-- PostgreSQL database dump
--

-- Dumped from database version 10.14
-- Dumped by pg_dump version 11.9

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: pg_cron; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS pg_cron WITH SCHEMA public;


--
-- Name: EXTENSION pg_cron; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION pg_cron IS 'Job scheduler for PostgreSQL';


--
-- Name: update_data_event(); Type: FUNCTION; Schema: public; Owner: lawfirm
--

CREATE FUNCTION public.update_data_event() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
row_count int;
    begin
    UPDATE tbl_events set is_active ='0'  WHERE  
    schedule_date = current_date 
    AND 
    schedule_time = to_char(current_timestamp,'HH24:MI')
    AND 
    is_active ='1';
    IF found THEN
     GET DIAGNOSTICS row_count = ROW_COUNT;
      RAISE NOTICE 'Update % row(s) FROM tbl_events', row_count;
         END IF;
         RETURN NULL;
         END;
$$;


ALTER FUNCTION public.update_data_event() OWNER TO lawfirm;

--
-- Name: account_seq; Type: SEQUENCE; Schema: public; Owner: lawfirm
--

CREATE SEQUENCE public.account_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.account_seq OWNER TO lawfirm;

--
-- Name: client_seq; Type: SEQUENCE; Schema: public; Owner: lawfirm
--

CREATE SEQUENCE public.client_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.client_seq OWNER TO lawfirm;

--
-- Name: employee_seq; Type: SEQUENCE; Schema: public; Owner: lawfirm
--

CREATE SEQUENCE public.employee_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.employee_seq OWNER TO lawfirm;

--
-- Name: engagement_seq; Type: SEQUENCE; Schema: public; Owner: lawfirm
--

CREATE SEQUENCE public.engagement_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.engagement_seq OWNER TO lawfirm;

--
-- Name: financial_seq; Type: SEQUENCE; Schema: public; Owner: lawfirm
--

CREATE SEQUENCE public.financial_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.financial_seq OWNER TO lawfirm;

--
-- Name: loan_seq; Type: SEQUENCE; Schema: public; Owner: lawfirm
--

CREATE SEQUENCE public.loan_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.loan_seq OWNER TO lawfirm;

--
-- Name: loantype_seq; Type: SEQUENCE; Schema: public; Owner: lawfirm
--

CREATE SEQUENCE public.loantype_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.loantype_seq OWNER TO lawfirm;

--
-- Name: ptkp_seq; Type: SEQUENCE; Schema: public; Owner: lawfirm
--

CREATE SEQUENCE public.ptkp_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.ptkp_seq OWNER TO lawfirm;

--
-- Name: reimburse_seq; Type: SEQUENCE; Schema: public; Owner: lawfirm
--

CREATE SEQUENCE public.reimburse_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.reimburse_seq OWNER TO lawfirm;

--
-- Name: reset_token_seq; Type: SEQUENCE; Schema: public; Owner: lawfirm
--

CREATE SEQUENCE public.reset_token_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.reset_token_seq OWNER TO lawfirm;

--
-- Name: role_seq; Type: SEQUENCE; Schema: public; Owner: lawfirm
--

CREATE SEQUENCE public.role_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.role_seq OWNER TO lawfirm;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: tbl_account; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_account (
    account_id bigint NOT NULL,
    account_name character varying(255),
    account_number character varying(255),
    account_number_finance character varying(255),
    bank_name character varying(255),
    is_active boolean,
    is_delete boolean,
    tgl_input timestamp without time zone,
    type_account character varying(255),
    id_employee bigint
);


ALTER TABLE public.tbl_account OWNER TO lawfirm;

--
-- Name: tbl_client_data; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_client_data (
    id_client bigint NOT NULL,
    address character varying(255),
    client_id character varying(255),
    client_name character varying(255),
    is_active character varying(1),
    npwp character varying(255),
    phone_number character varying(255),
    pic character varying(255),
    tgl_input timestamp without time zone
);


ALTER TABLE public.tbl_client_data OWNER TO lawfirm;

--
-- Name: tbl_disburse_loan; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_disburse_loan (
    disbursement_uu_id character varying(255) NOT NULL,
    bulan_input character varying(10),
    disburse_date date,
    disbursement_amount double precision,
    disbursement_id character varying(255),
    is_active character varying(1),
    signature character varying(255),
    tahun_input character varying(10),
    tgl_input timestamp without time zone,
    id_loan bigint
);


ALTER TABLE public.tbl_disburse_loan OWNER TO lawfirm;

--
-- Name: tbl_disbursement; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_disbursement (
    disbursement_uu_id character varying(255) NOT NULL,
    bulan_input character varying(10),
    disburse_date date,
    disbursement_amount double precision,
    disbursement_id character varying(255),
    signature character varying(255),
    tahun_input character varying(10),
    tgl_input timestamp without time zone,
    engagement_id bigint,
    is_active character varying(1),
    number_of_disbursement bigint,
    cut_off_date date,
    old_cut_off_date date
);


ALTER TABLE public.tbl_disbursement OWNER TO lawfirm;

--
-- Name: tbl_document_case; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_document_case (
    case_document_id character varying(255) NOT NULL,
    date_input timestamp without time zone,
    document_type character varying(255),
    is_active character varying(1),
    link_document character varying(255),
    engagement_id bigint,
    title character varying(100)
);


ALTER TABLE public.tbl_document_case OWNER TO lawfirm;

--
-- Name: tbl_document_reimburse; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_document_reimburse (
    reimburse_document_id character varying(255) NOT NULL,
    approved_by character varying(255),
    aprroved_date date,
    date_input timestamp without time zone,
    description character varying(255),
    link_document character varying(255),
    record_date date,
    status character varying(1),
    reimburse_id bigint
);


ALTER TABLE public.tbl_document_reimburse OWNER TO lawfirm;

--
-- Name: tbl_employee; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_employee (
    id_employee bigint NOT NULL,
    address character varying(255),
    approved_date timestamp without time zone,
    date_register date,
    email character varying(255),
    employee_id character varying(255),
    gender character varying(255),
    is_active boolean,
    is_delete boolean,
    is_login boolean,
    link_cv character varying(255),
    loan_amount double precision,
    cell_phone character varying(255),
    name character varying(255),
    nik character varying(255),
    npwp character varying(255),
    out_standing_loan double precision,
    user_pass character varying(255),
    role_name character varying(255),
    salary double precision,
    sign_ttd character varying(255),
    status character varying(10),
    tax_status character varying(255),
    tgl_input timestamp without time zone,
    user_name character varying(255),
    approved_by bigint,
    date_resign date,
    is_five_minutes character varying(4),
    time_five_minutes timestamp(0) without time zone
);


ALTER TABLE public.tbl_employee OWNER TO lawfirm;

--
-- Name: tbl_employee_role; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_employee_role (
    role_id bigint NOT NULL,
    dsc character varying(255),
    is_active boolean,
    role_name character varying(255)
);


ALTER TABLE public.tbl_employee_role OWNER TO lawfirm;

--
-- Name: tbl_engagement; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_engagement (
    dtype character varying(80) NOT NULL,
    engagement_id bigint NOT NULL,
    approved_by character varying(255),
    approved_date timestamp without time zone,
    caseid character varying(20),
    created_date timestamp without time zone,
    invoice_number character varying(255),
    is_active character varying(1),
    signature character varying(255),
    status character varying(255),
    tahun_input character varying(10),
    case_over_view character varying(255),
    event_date date,
    event_time character varying(10),
    note character varying(255),
    panitera character varying(255),
    profesional_fee double precision,
    profesional_fee_net double precision,
    strategy character varying(255),
    target_achievement character varying(255),
    id_client bigint,
    id_employee bigint,
    dmp_portion double precision,
    dmp_percent bigint,
    closed_by character varying(40),
    closed_date date,
    disburse_by character varying(20),
    disburse_date date
);


ALTER TABLE public.tbl_engagement OWNER TO lawfirm;

--
-- Name: tbl_engagement_history; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_engagement_history (
    id character varying(255) NOT NULL,
    is_active character varying(1),
    response character varying(255),
    tgl_input timestamp without time zone NOT NULL,
    user_id bigint,
    engagement_id bigint
);


ALTER TABLE public.tbl_engagement_history OWNER TO lawfirm;

--
-- Name: tbl_events; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_events (
    event_id character varying(255) NOT NULL,
    date_input timestamp without time zone,
    event_name character varying(20),
    event_type character varying(1),
    is_active character varying(1),
    schedule_date date,
    schedule_time character varying(10),
    engagement_id bigint
);


ALTER TABLE public.tbl_events OWNER TO lawfirm;

--
-- Name: tbl_financial; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_financial (
    financial_id bigint NOT NULL,
    created_date timestamp without time zone,
    disburse_date timestamp without time zone,
    nett_profit double precision,
    out_standing double precision,
    pajak double precision,
    pkp double precision,
    salary double precision,
    signature character varying(255),
    status character varying(1),
    id_employee bigint,
    engagement_id bigint,
    loan_id character varying(255)
);


ALTER TABLE public.tbl_financial OWNER TO lawfirm;

--
-- Name: tbl_loan; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_loan (
    id bigint NOT NULL,
    aproved_by_admin character varying(255),
    aproved_by_finance character varying(255),
    date_approved timestamp without time zone,
    date_approved_by_finance timestamp without time zone,
    date_created date,
    date_month character varying(255),
    disburse_date date,
    is_active character varying(1),
    is_delete boolean,
    loan_amount double precision,
    loan_id character varying(255),
    outstanding double precision,
    repayment_date date,
    status character varying(1),
    tgl_input character varying(10),
    id_employee bigint,
    engagement_id bigint,
    loan_type_id bigint,
    signature character varying(255)
);


ALTER TABLE public.tbl_loan OWNER TO lawfirm;

--
-- Name: tbl_loan_history; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_loan_history (
    id character varying(255) NOT NULL,
    is_active character varying(1),
    response character varying(255),
    tgl_input timestamp without time zone NOT NULL,
    user_id bigint,
    id_loan bigint
);


ALTER TABLE public.tbl_loan_history OWNER TO lawfirm;

--
-- Name: tbl_loan_type; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_loan_type (
    loan_type_id bigint NOT NULL,
    dsc character varying(255),
    is_active boolean,
    loan_type character varying(255)
);


ALTER TABLE public.tbl_loan_type OWNER TO lawfirm;

--
-- Name: tbl_member; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_member (
    member_id character varying(255) NOT NULL,
    fee_share double precision,
    tgl_input timestamp without time zone,
    id_employee bigint,
    team_member_id bigint,
    amount_portion double precision,
    status character varying(5)
);


ALTER TABLE public.tbl_member OWNER TO lawfirm;

--
-- Name: tbl_outstanding_loan_a; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_outstanding_loan_a (
    outstanding_a_uu_id character varying(255) NOT NULL,
    cut_off_date date,
    disburse_id character varying(10),
    disburseable_amount double precision,
    id_employee bigint,
    total_loan_amount double precision,
    outstanding_after_disbursement_amount double precision,
    tax_year character varying(10),
    is_active boolean,
    number_disbursement bigint
);


ALTER TABLE public.tbl_outstanding_loan_a OWNER TO lawfirm;

--
-- Name: tbl_outstanding_loan_b; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_outstanding_loan_b (
    outstanding_id character varying(255),
    reimburse_id bigint,
    tahun_input character varying(10),
    tgl_input timestamp without time zone,
    loan_id bigint,
    user_id bigint,
    total_reimburse_amount double precision,
    case_id character varying(50),
    is_active boolean,
    total_loan_amount double precision,
    out_standing double precision,
    outstanding_b_uu_id character varying(255) NOT NULL,
    id_employee bigint
);


ALTER TABLE public.tbl_outstanding_loan_b OWNER TO lawfirm;

--
-- Name: tbl_password_reset_token; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_password_reset_token (
    id_token bigint NOT NULL,
    expiry_date date,
    token character varying(255),
    id_employee bigint NOT NULL
);


ALTER TABLE public.tbl_password_reset_token OWNER TO lawfirm;

--
-- Name: tbl_period; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_period (
    id character varying(255) NOT NULL,
    bulan_disburse character varying(10),
    case_id character varying(10),
    disburse_id character varying(10),
    id_employee bigint,
    income_tax_paid_on_prior_period double precision,
    number_disbursement integer,
    prev_disbursement double precision,
    tax_year character varying(10),
    employee_id character varying(10),
    status character varying(1)
);


ALTER TABLE public.tbl_period OWNER TO lawfirm;

--
-- Name: tbl_professional; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_professional (
    uuid character varying(255) NOT NULL,
    amount double precision,
    input_date timestamp without time zone,
    perecentage double precision,
    status character varying(1),
    engagement_id bigint,
    id_employee bigint
);


ALTER TABLE public.tbl_professional OWNER TO lawfirm;

--
-- Name: tbl_ptkp; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_ptkp (
    id_tax bigint NOT NULL,
    ptkp double precision,
    tax_status character varying(10),
    is_active character varying(1)
);


ALTER TABLE public.tbl_ptkp OWNER TO lawfirm;

--
-- Name: tbl_reimbursement; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_reimbursement (
    reimburse_id bigint NOT NULL,
    approved_amount double precision,
    approved_by bigint,
    approved_date timestamp without time zone,
    expense_date date,
    note character varying(255),
    reimburse_amount double precision,
    reimbursed_by bigint,
    reimbursed_date timestamp without time zone,
    reimbursement_id character varying(30),
    signature character varying(255),
    status character varying(10),
    id_employee bigint,
    id_loan bigint,
    is_active character varying(1),
    link_document character varying(120),
    tgl_input timestamp without time zone,
    remarks character varying(150),
    mime_type character varying(10)
);


ALTER TABLE public.tbl_reimbursement OWNER TO lawfirm;

--
-- Name: tbl_reimbursement_history; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_reimbursement_history (
    id character varying(255) NOT NULL,
    is_active character varying(1),
    response character varying(255),
    tgl_input timestamp without time zone NOT NULL,
    user_id bigint,
    reimburse_id bigint
);


ALTER TABLE public.tbl_reimbursement_history OWNER TO lawfirm;

--
-- Name: tbl_tax; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_tax (
);


ALTER TABLE public.tbl_tax OWNER TO lawfirm;

--
-- Name: tbl_team_member; Type: TABLE; Schema: public; Owner: lawfirm
--

CREATE TABLE public.tbl_team_member (
    team_member_id bigint NOT NULL,
    description character varying(255),
    dmp_id bigint,
    fee_share double precision,
    is_active boolean,
    tahun_input character varying(10),
    engagement_id bigint,
    amount_portion double precision,
    status character varying(5)
);


ALTER TABLE public.tbl_team_member OWNER TO lawfirm;

--
-- Name: team_member_seq; Type: SEQUENCE; Schema: public; Owner: lawfirm
--

CREATE SEQUENCE public.team_member_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.team_member_seq OWNER TO lawfirm;

--
-- Data for Name: job; Type: TABLE DATA; Schema: cron; Owner: postgres
--

COPY cron.job (jobid, schedule, command, nodename, nodeport, database, username, active, jobname) FROM stdin;
\.


--
-- Data for Name: job_run_details; Type: TABLE DATA; Schema: cron; Owner: postgres
--

COPY cron.job_run_details (jobid, runid, job_pid, database, username, command, status, return_message, start_time, end_time) FROM stdin;
\.


--
-- Data for Name: tbl_account; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_account (account_id, account_name, account_number, account_number_finance, bank_name, is_active, is_delete, tgl_input, type_account, id_employee) FROM stdin;
1	admin1		035321461129229	BCA	t	f	2020-06-28 14:02:26.459	payroll	2
2	admin1		08135221126249	BRI	t	f	2020-06-28 14:02:26.46	loan	2
3	lawyer1		BCA-lawyer1	BCA	t	f	2020-09-01 11:34:19.499	payroll	3
4	lawyer1		BRI-lawyer1	BRI	t	f	2020-09-01 11:34:19.5	loan	3
5	lawyer2		BCA-lawyer2	BCA	t	f	2020-09-01 11:40:05.061	payroll	4
6	lawyer2		BNI-lawyer2	BNI	t	f	2020-09-01 11:40:05.061	loan	4
7	finance1		BCA-finance1	BCA	t	f	2020-09-01 11:43:34.136	payroll	5
8	finance1		BNI-finance1	BNI	t	f	2020-09-01 11:43:34.136	loan	5
9	dmp1		BCA-dmp1	BCA	t	f	2020-09-01 13:25:48.713	payroll	6
10	dmp1		BRI-dmp1	BRI	t	f	2020-09-01 13:25:48.713	loan	6
11	lawyer3		BCA-lawyer3	BCA	t	f	2020-09-01 15:48:02.715	payroll	7
12	lawyer3		BRI-lawyer3	BRI	t	f	2020-09-01 15:48:02.716	loan	7
13	lawyer4		BCA-lawyer4	BCA	t	f	2020-09-01 15:50:44.686	payroll	8
14	lawyer4		BRI-lawyer4	BRI	t	f	2020-09-01 15:50:44.686	loan	8
15	dmp-2		BCA-dmp-2	BCA	t	f	2020-09-02 14:09:40.825	payroll	9
16	dmp-2		BNI-dmp-2	BCA	t	f	2020-09-02 14:09:40.826	loan	9
18	lawyer5	BCAlawyer5	\N	BCA	f	f	2020-11-04 07:14:05.168	loan	10
17	lawyer5		BCAlawyer5	BCA	t	f	2020-11-04 07:14:05.166	payroll	10
20	LAWYER6	BCALAWYER61	\N	BCA	f	f	2020-11-04 07:16:55.878	loan	11
19	LAWYER6		BCALAWYER6	BCA	t	f	2020-11-04 07:16:55.878	payroll	11
22	lawyer7	BCAlawyer72	\N	BCA	f	f	2020-11-11 16:52:18.855	loan	12
21	lawyer7		BCAlawyer71	BCA	t	f	2020-11-11 16:52:18.854	payroll	12
24	lawyer8	BCAlawyer82	\N	BCA	f	f	2020-11-11 16:59:37.471	loan	13
23	lawyer8		BCAlawyer81	BCA	t	f	2020-11-11 16:59:37.47	payroll	13
\.


--
-- Data for Name: tbl_client_data; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_client_data (id_client, address, client_id, client_name, is_active, npwp, phone_number, pic, tgl_input) FROM stdin;
19	Jakarta Raya	CLIENT001	PT. ABC	1	123456789123456	\N	Pic 10112020	2020-10-10 16:23:37
20	Jakarta Selatan	CLIENT002	PT. DEF	1	123546538123123	\N	tester 11112020	2020-11-11 17:20:30.682
\.


--
-- Data for Name: tbl_disburse_loan; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_disburse_loan (disbursement_uu_id, bulan_input, disburse_date, disbursement_amount, disbursement_id, is_active, signature, tahun_input, tgl_input, id_loan) FROM stdin;
\.


--
-- Data for Name: tbl_disbursement; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_disbursement (disbursement_uu_id, bulan_input, disburse_date, disbursement_amount, disbursement_id, signature, tahun_input, tgl_input, engagement_id, is_active, number_of_disbursement, cut_off_date, old_cut_off_date) FROM stdin;
17d1a4b4-ffe9-4902-80c2-2e05f9e5df93	MARET	2020-03-12	\N	DSBMAR2020	\N	2020	2020-03-12 17:13:23	19	1	1	2020-03-12	2020-03-12
9d67bc4d-cb1f-449d-ba00-92cb624298dd	MARET	2020-03-12	\N	DSBMAR2020	\N	2020	2020-03-12 17:13:17	18	1	1	2020-03-12	2020-03-12
\.


--
-- Data for Name: tbl_document_case; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_document_case (case_document_id, date_input, document_type, is_active, link_document, engagement_id, title) FROM stdin;
\.


--
-- Data for Name: tbl_document_reimburse; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_document_reimburse (reimburse_document_id, approved_by, aprroved_date, date_input, description, link_document, record_date, status, reimburse_id) FROM stdin;
\.


--
-- Data for Name: tbl_employee; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_employee (id_employee, address, approved_date, date_register, email, employee_id, gender, is_active, is_delete, is_login, link_cv, loan_amount, cell_phone, name, nik, npwp, out_standing_loan, user_pass, role_name, salary, sign_ttd, status, tax_status, tgl_input, user_name, approved_by, date_resign, is_five_minutes, time_five_minutes) FROM stdin;
2	Perumahan admin 1, Jalan Joglo Raya no 1	2019-06-28 14:02:26	2019-07-16	admin@gmail.com	ADM001	m	t	f	f	/opt/lawfirm/UploadFile/employee/2/Building+a+REST+API+with+Spring.pdf	50000000	081324212624	admin1	1120262127240011	019102177722610	\N	$2a$10$N2WI8gcpay8tX3cYmcUBUuisbr.vbPbMhq2YWdFMDkzrZK089A3U2	admin	0	\N	a	TK0	2019-06-28 14:02:26	admin@gmail.com	1	\N	\N	\N
7	Jakarta	2019-09-01 15:48:02	2019-08-19	lawyer3@gmail.com	LAW003	m	t	f	f	/opt/lawfirm/UploadFile/employee/7/76323741-cv.pdf	8000000	081317123945	Lawyer-3	NIK-lawyer-3	NPWP-lawyer-3	\N	$2a$10$rbzVpt4yZE0qP/Eaq6.jfOrlMXXV6MH1xdKAG9fEYByrHTsCWiWXS	lawyer	4000000	\N	a	TK0	2019-09-01 15:48:02	lawyer3@gmail.com	2	\N	\N	\N
8	Jakarta	2019-09-01 15:50:44	2019-08-25	lawyer4@gmail.com	LAW004	m	t	f	f	/opt/lawfirm/UploadFile/employee/8/sample_form.pdf	8000000	085718372912	Lawyer-4	NIK-Lawyer-4	NPWP-Lawyer-4	\N	$2a$10$o2ghTny3H/HR8kaWQ/qWaeTPYVDAWfUZF/rQXHsXjXGjxi/z/PJiC	lawyer	4000000	\N	a	TK0	2019-09-01 15:50:44	lawyer4@gmail.com	2	\N	\N	\N
1	\N	2019-06-28 13:59:37	2019-07-11	sysadmin@gmail.com	SYS001	m	t	f	f	\N	\N	\N	sysadmin	\N	\N	\N	$2a$10$V0SFp9rI7K5tphgCBsQTyOpfKMQU7ypob.SNXtuw5Yvw.6HgnB5h2	sysadmin	0	\N	a	\N	2019-06-28 13:59:37	sysadmin	\N	\N	\N	\N
9	Jakarta	2019-09-02 14:09:40	2019-07-01	dmp2@gmail.com	DMP002	m	t	f	f	/opt/lawfirm/UploadFile/employee/9/Slide-LSE-23.pdf	25000000	081329182317	dmp-2	NIK-DMP-2	NPWP-DMP-2	\N	$2a$10$IzvZoMW5801PgHYLMVGZWuaFiARTYXwwYA6SgB7OI2ba/KRCPAK.K	dmp	4000000	\N	a	K3	2019-09-02 14:09:40	dmp2@gmail.com	2	\N	\N	\N
10	Jakarta	2020-11-04 07:14:05.252	2019-10-01	lawyer5@gmail.com	LAW005	m	t	f	f	/opt/lawfirm/UploadFile/employee/10/Building+a+REST+API+with+Spring23.pdf	15000000	081317283918	lawyer5	1234567890123456	123456789012345	\N	$2a$10$Fj69c0C0bvtC7G9mVp.D7eaVm.R0lcwwoDfBqj.f8XoPu9XzFdury	lawyer	4000000	\N	a	K1	2020-11-04 07:14:05.161	lawyer5@gmail.com	2	\N	\N	\N
11	JakartaSelatan	2020-11-04 07:16:55.944	2019-09-01	lawyer6@gmail.com	LAW006	m	t	f	f	/opt/lawfirm/UploadFile/employee/11/Building+a+REST+API+with+Spring.pdf	15000000	081315123819	lawyer6	1234567840123456	1234367890123456	\N	$2a$10$/ah9BYkW8mhvOIssalUWpe0sugjrjCxXBtOGOnRAdcmISheO8OR4S	lawyer	4000000	\N	a	TK0	2020-11-04 07:16:55.877	lawyer6@gmail.com	2	\N	\N	\N
13	Jakarta	2020-11-11 16:59:37.532	2020-01-03	lawyer8@gmail.com	LAW008	m	t	f	f	\N	8000000	081371823718	lawyer8	1234567890987654	123454312345678	\N	$2a$10$5kZVR5aNgOvHXUbwEABiBe.czsnEo39L.XclG8AQ4qPwi4ZLvD10u	lawyer	4000000	\N	a	TK0	2020-11-11 16:59:37.468	lawyer8@gmail.com	2	\N	\N	\N
3	jl. Jakarta	2019-09-01 11:34:19	2019-09-01	lawyer1@gmail.com	LAW001	m	t	f	f	/opt/lawfirm/UploadFile/employee/3/contoh-cv-pdf-bahasa.pdf	15000000	081318123412	Lawyer-1	NIK-Lawyer1	NPWP-Lawyer1	\N	$2a$10$F.xyRztEjUr3Wr3Ru8awNOX7XAMrtstntz5xDicNK.ZhgoquxobRa	lawyer	4000000	\N	a	TK0	2019-09-01 11:34:19	lawyer1@gmail.com	2	\N	\N	\N
5	Jakarta	2019-09-01 11:43:34	2019-07-01	finance1@gmail.com	FIN001	m	t	f	f	/opt/lawfirm/UploadFile/employee/5/finance-1_CV.pdf	50000000	0813182312319	finance-1	NIK-Finance-1	NPWP-Finance-1	\N	$2a$10$iRX/msVEFJxvBuvxY5CYkO/JdI0vZIYiRxEeXZdW72RugHNW7YZk.	finance	4000000	\N	a	K0	2019-09-01 11:43:34	finance1@gmail.com	2	\N	\N	\N
12	Jakarta	2020-11-11 16:52:18.923	2019-08-01	lawyer7@gmail.com	LAW007	m	t	f	f	\N	8000000	81239283128	lawyer7	1213456789001234	123455566778901	\N	$2a$10$8UHYC46zuaqC3xX2NEpjo.8sFG1/0whIXmWgb0WpeeHeksJ5DojOS	lawyer	4000000	\N	a	K1	2020-11-11 16:52:18.852	lawyer7@gmail.com	2	\N	\N	\N
6	jakarta	2019-09-01 13:25:48	2019-06-01	dmp1@gmail.com	DMP001	m	t	f	f	/opt/lawfirm/UploadFile/employee/6/127695695-Contoh-Resume-Untuk-Latihan-Industri.pdf	30000000	081237281921	dmp-1	NIK-dmp1	NPWP-dmp1	\N	$2a$10$Ej/vJoSc264uh.sK.7npAOg9dig463YXwCX5MXhotNp1skSbjUZNy	dmp	4000000	\N	a	K2	2019-09-01 13:25:48	dmp1@gmail.com	2	\N	\N	\N
4	Jakarta	2019-09-01 11:40:05	2019-08-31	lawyer2@gmail.com	LAW002	f	t	f	f	/opt/lawfirm/UploadFile/employee/4/76323741-Contoh-CV.pdf	15000000	081319283218	Lawyer-2	NIK-Lawyer-2	NPWP-Lawyer-2	\N	$2a$10$I/BomcXTtilLSNsazIubkeDnXoxzQlPQAAXEQQWAFgH4j6r4cQP5.	lawyer	4000000	\N	a	TK0	2019-09-01 11:40:05	lawyer2@gmail.com	2	\N	\N	\N
\.


--
-- Data for Name: tbl_employee_role; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_employee_role (role_id, dsc, is_active, role_name) FROM stdin;
1	sysadmin	t	sysadmin
2	admin	t	admin
3	finance	t	finance
4	deputy manager	t	dmp
5	lawyer	t	lawyer
6	support	t	support
\.


--
-- Data for Name: tbl_engagement; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_engagement (dtype, engagement_id, approved_by, approved_date, caseid, created_date, invoice_number, is_active, signature, status, tahun_input, case_over_view, event_date, event_time, note, panitera, profesional_fee, profesional_fee_net, strategy, target_achievement, id_client, id_employee, dmp_portion, dmp_percent, closed_by, closed_date, disburse_by, disburse_date) FROM stdin;
CaseDetails	19	2	2020-11-11 17:21:23.825	CASE2002	2020-11-11 17:20:30	\N	4	\N	closed	20	Case Overview 2	\N	\N	Notes 1	Pengadilan Negri Jakarta Utara	1000000000	750000000	Strategy 2	\N	20	9	300000000	40	ADM001	2020-11-12	5	2020-11-12
CaseDetails	18	2	2020-09-10 16:24:46	CASE2001	2020-01-10 16:23:37	\N	4	\N	closed	20	Case Overview	\N	\N	Notes 1	Pengadilan Negri Jakarta Selatan	1000000000	750000000	Strategy 1	\N	19	6	300000000	40	ADM001	2020-11-12	5	2020-11-12
\.


--
-- Data for Name: tbl_engagement_history; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_engagement_history (id, is_active, response, tgl_input, user_id, engagement_id) FROM stdin;
ff5ac04d-db83-4b70-829f-a0d5689c07b7	\N	approve	2020-11-10 16:24:46.651	2	18
9ebae288-72ab-4f4c-95eb-6880996d33dc	\N	closed By : ADM001	2020-11-10 17:38:38.308	2	18
fa30cefd-4890-40c8-8457-673dee3c2781	\N	approve	2020-11-11 17:21:23.939	2	19
8953385a-d80f-4bed-8e65-8e1dbc067281	\N	closed By : ADM001	2020-11-11 17:34:25.355	2	19
09b21292-f915-4413-98d2-53519bc494fb	\N	closed By : ADM001	2020-11-12 17:00:01.917	2	19
1edf6b50-1240-470f-9371-df7f5db4b6b6	\N	closed By : ADM001	2020-11-12 17:13:17.26	2	18
c0c411ca-0051-4dc6-9ebb-df11340183b3	\N	closed By : ADM001	2020-11-12 17:13:23.776	2	19
\.


--
-- Data for Name: tbl_events; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_events (event_id, date_input, event_name, event_type, is_active, schedule_date, schedule_time, engagement_id) FROM stdin;
\.


--
-- Data for Name: tbl_financial; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_financial (financial_id, created_date, disburse_date, nett_profit, out_standing, pajak, pkp, salary, signature, status, id_employee, engagement_id, loan_id) FROM stdin;
\.


--
-- Data for Name: tbl_loan; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_loan (id, aproved_by_admin, aproved_by_finance, date_approved, date_approved_by_finance, date_created, date_month, disburse_date, is_active, is_delete, loan_amount, loan_id, outstanding, repayment_date, status, tgl_input, id_employee, engagement_id, loan_type_id, signature) FROM stdin;
64	2	5	2020-01-10 16:33:09	2020-01-10 16:33:27	2020-01-10	\N	2020-01-10	4	f	10000000	BCS200103	\N	2020-12-31	d	2020	6	18	2	\N
65	2	5	2020-02-11 14:28:54	2020-02-10 14:37:32	2020-02-10	022020	2020-02-10	4	f	15000000	ALAW0022002	\N	2020-12-31	d	2020	4	\N	1	\N
66	2	5	2020-02-27 16:34:19	2020-02-28 16:34:44	2020-02-27	022020	2020-02-10	4	f	25000000	ADMP0022001	\N	2020-12-31	d	2020	9	\N	1	\N
67	2	5	2020-02-27 16:42:13	2020-02-28 16:43:12	2020-02-27	022020	2020-02-27	4	f	25000000	ADMP0022002	\N	2020-12-31	d	2020	9	\N	1	\N
53	2	5	2020-01-10 14:20:40	2020-01-12 14:23:07	2020-01-10	012020	2020-01-12	4	f	30000000	ADMP0012001	\N	2020-12-31	d	2020	6	\N	1	\N
54	2	5	2020-02-10 14:25:35	2020-02-12 14:26:49	2020-02-10	022020	2020-02-10	4	f	30000000	ADMP0012002	\N	2020-12-31	d	2020	6	\N	1	\N
56	2	5	2020-01-10 14:32:21	2020-01-10 14:32:53	2020-01-10	022020	2020-02-10	4	f	15000000	ALAW0012002	\N	2020-12-31	d	2020	3	\N	1	\N
68	2	5	2020-01-27 16:45:46	2020-01-28 16:46:15	2020-01-27	012020	2020-01-28	4	f	15000000	ALAW0052001	\N	2020-12-31	d	2020	10	\N	1	\N
69	2	5	2020-02-27 16:48:11	2020-02-28 16:48:35	2020-02-27	022020	2020-02-27	4	f	15000000	ALAW0052002	\N	2020-12-31	d	2020	10	\N	1	\N
55	2	5	2020-01-11 14:28:54	2020-01-12 14:30:03	2020-01-10	012020	2020-01-12	4	f	15000000	ALAW0012001	\N	2020-12-31	d	2020	3	\N	1	\N
70	2	5	2020-01-27 17:02:33	2020-01-28 17:03:24	2020-01-27	012020	2020-01-28	4	f	15000000	ALAW0062001	\N	2020-12-31	d	2020	11	\N	1	\N
71	2	5	2020-02-27 17:05:32	2020-02-28 17:06:30	2020-02-27	022020	2020-02-27	4	f	15000000	ALAW0062002	\N	2020-11-30	d	2020	11	\N	1	\N
72	2	5	2020-01-27 17:10:03	2020-01-28 17:10:24	2020-01-27	012020	2020-01-28	4	f	8000000	ALAW0072001	\N	2020-12-31	d	2020	12	\N	1	\N
73	2	5	2020-02-27 17:12:14	2020-02-28 17:12:54	2020-02-27	022020	2020-02-27	4	f	8000000	ALAW0072002	\N	2020-12-31	d	2020	12	\N	1	\N
74	2	5	2020-01-27 17:13:14	2020-01-28 17:13:24	2020-01-27	012020	2020-01-28	1	f	8000000	ALAW0082001	\N	2020-12-31	d	2020	13	\N	1	\N
59	2	5	2020-02-10 14:51:07	2020-02-12 14:52:23	2020-02-10	022020	2020-02-10	4	f	8000000	ALAW0032002	\N	2020-12-31	d	2020	7	\N	1	\N
75	2	5	2020-02-27 17:14:44	2020-02-28 17:15:54	2020-02-27	022020	2020-02-27	1	f	8000000	ALAW0082002	\N	2020-12-31	d	2020	13	\N	1	\N
76	2	5	2020-01-11 17:22:26	2020-01-11 17:23:03	2020-01-11	\N	2020-01-11	4	f	30000000	BCS200201	\N	2020-12-31	d	2020	9	19	2	\N
57	2	5	2020-01-10 14:37:08	2020-01-11 14:37:32	2020-01-10	012020	2020-01-10	4	f	15000000	ALAW0022001	\N	2020-12-31	d	2020	4	\N	1	\N
77	2	5	2020-02-11 17:25:49	2020-02-11 17:26:05	2020-02-11	\N	2020-02-11	4	f	20000000	BCS200202	\N	2020-12-31	d	2020	9	19	2	\N
78	2	5	2020-02-11 17:28:33	2020-02-11 17:28:47	2020-02-11	\N	2020-02-11	4	f	20000000	BCS200203	\N	2020-12-31	d	2020	9	19	2	\N
58	2	5	2020-01-11 14:43:02	2020-01-12 14:43:52	2020-01-10	012020	2020-01-10	4	f	8000000	ALAW0032001	\N	2020-12-31	d	2020	7	\N	1	\N
60	2	5	2020-01-10 16:16:45	2020-01-12 16:17:18	2020-01-10	012020	2020-01-10	4	f	8000000	ALAW0042001	\N	2020-12-31	d	2020	8	\N	1	\N
61	2	5	2020-02-10 16:18:41	2020-02-11 16:18:58	2020-02-10	022020	2020-02-11	4	f	8000000	ALAW0042002	\N	2020-12-31	d	2020	8	\N	1	\N
62	2	5	2020-01-11 16:29:10	2020-01-12 16:29:41	2020-01-11	\N	2020-01-11	4	f	50000000	BCS200101	\N	2020-12-31	d	2020	6	18	2	\N
63	2	5	2020-02-10 16:30:42	2020-02-10 16:32:12	2020-02-10	\N	2020-02-11	4	f	10000000	BCS200102	\N	2020-12-31	d	2020	6	18	2	\N
\.


--
-- Data for Name: tbl_loan_history; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_loan_history (id, is_active, response, tgl_input, user_id, id_loan) FROM stdin;
599b9028-ebc3-46e0-bb97-13aae2fe0434	\N	approve	2020-11-10 14:20:40.92	2	53
5cefadb6-215a-4b22-89e4-cf381b97ea50	\N	disburse by : FIN001	2020-11-10 14:23:07.346	5	53
a1d6c969-acee-40ae-afd6-552b6d87bae6	\N	approve	2020-11-10 14:25:35.85	2	54
f49298ca-5b9c-413f-89fd-e9fc0cfa672e	\N	disburse by : FIN001	2020-11-10 14:26:49.549	5	54
d472d2b9-317c-46bc-b062-3712811acf2d	\N	approve	2020-11-10 14:28:54.099	2	55
c4812be1-e835-4cfd-89d3-265c45b273f0	\N	disburse by : FIN001	2020-11-10 14:30:03.759	5	55
53982fa3-ae77-4418-819b-03718d8ccbbe	\N	approve	2020-11-10 14:32:22.034	2	56
dd5bd370-d3d2-4baf-a55f-897962d0e44b	\N	disburse by : FIN001	2020-11-10 14:32:53.061	5	56
b693e0f0-7b9e-4571-9138-f70fb70c44cc	\N	approve	2020-11-10 14:37:08.966	2	57
6f18c0cd-c18c-4d93-afb1-9e29dcca5a16	\N	disburse by : FIN001	2020-11-10 14:37:32.919	5	57
485b82cb-eca2-4cac-92e3-a059b87effd1	\N	approve	2020-11-10 14:43:02.131	2	58
182e73fe-6829-4c67-bb39-342cee806689	\N	disburse by : FIN001	2020-11-10 14:43:52.62	5	58
ca913be4-a018-40a4-986a-6145b2094f56	\N	approve	2020-11-10 14:51:07.999	2	59
1b90a899-c70b-49f6-a15e-5cab1b04715c	\N	disburse by : FIN001	2020-11-10 14:52:23.494	5	59
bfba8de2-0a54-43e1-b9d9-44d2fb1ec8aa	\N	approve	2020-11-10 16:16:45.564	2	60
b6530111-014f-4945-861c-d62911dbf160	\N	disburse by : FIN001	2020-11-10 16:17:19.019	5	60
72dae43e-badc-4f97-ad40-e301e67165f5	\N	approve	2020-11-10 16:18:41.598	2	61
712d8fb6-33f9-4490-a647-8d64be797108	\N	disburse by : FIN001	2020-11-10 16:18:58.105	5	61
af73316f-3178-4c79-b4ef-55fc78c9f3cb	\N	approve	2020-11-10 16:29:10.463	2	62
3466eef7-0e6a-431a-8c0c-4bee17e01f1a	\N	disburse by : FIN001	2020-11-10 16:29:41.07	5	62
148ee56e-c5d4-401c-b59f-2d0451c175c3	\N	approve	2020-11-10 16:30:43.026	2	63
58e1c602-7532-489f-9aab-47961d5874a4	\N	disburse by : FIN001	2020-11-10 16:32:12.342	5	63
2e201bcc-fa69-40f1-9d97-d3a596c1f39e	\N	approve	2020-11-10 16:33:09.912	2	64
5700a64c-9ed5-4f37-b1bc-97564d3c0217	\N	disburse by : FIN001	2020-11-10 16:33:27.37	5	64
1f5b3acf-ea06-45fc-8083-1f2aea138a93	\N	approve	2020-11-11 16:34:19.181	2	66
14cc82fb-c9f3-4b5e-9ee7-8d0ade9d21a1	\N	disburse by : FIN001	2020-11-11 16:34:44.079	5	66
8dac671a-81ee-4ccd-82d2-d28759eba36b	\N	approve	2020-11-11 16:42:13.963	2	67
684ddc44-f607-4b43-9974-0a2ceaa663f8	\N	disburse by : FIN001	2020-11-11 16:43:12.379	5	67
8b8b7c6d-3f11-4c13-8c76-cb569aa846f2	\N	approve	2020-11-11 16:45:46.303	2	68
137e59cb-eab7-444d-8c00-81655c87e5ee	\N	disburse by : FIN001	2020-11-11 16:46:15.05	5	68
70f7abb5-a264-4bdf-8624-984ae1257314	\N	approve	2020-11-11 16:48:11.52	2	69
19a85986-2231-4eee-bb87-0ae45dda8b84	\N	disburse by : FIN001	2020-11-11 16:48:35.202	5	69
cd7b1967-d771-474e-aa0d-d562db56b9a7	\N	approve	2020-11-11 17:02:33.285	2	70
87a8d410-6691-40fd-b826-7a2de16460e4	\N	disburse by : FIN001	2020-11-11 17:03:24.547	5	70
1473a17b-ae55-404d-8bcf-67c2f922fbad	\N	approve	2020-11-11 17:05:32.807	2	71
b11dac81-4978-47dd-b982-772ce543bff8	\N	disburse by : FIN001	2020-11-11 17:06:30.384	5	71
540afd7a-c218-4fca-9f3e-57afdd143f8e	\N	approve	2020-11-11 17:10:03.206	2	72
788375d2-70f3-4019-8a1a-09956cbb8c07	\N	disburse by : FIN001	2020-11-11 17:10:24.297	5	72
368a3d03-db5c-410d-ac4c-a252e86ddd40	\N	approve	2020-11-11 17:12:14.659	2	73
62e0fb66-4aa0-455f-a2a3-f653d3513c4a	\N	disburse by : FIN001	2020-11-11 17:12:54.598	5	73
184458b9-e19a-4ba2-ad4d-82be0966c255	\N	approve	2020-11-11 17:22:26.508	2	76
2191886f-f6ab-4220-a3c7-8b694914b176	\N	disburse by : FIN001	2020-11-11 17:23:04.028	5	76
ea09d89c-0396-4da5-b330-e0d9d36280d9	\N	approve	2020-11-11 17:25:49.618	2	77
e169b40e-37e3-4f8d-8edd-79be3de5be4c	\N	disburse by : FIN001	2020-11-11 17:26:05.206	5	77
dc01ec20-f5d7-4c52-8949-5d8a9b14d2cc	\N	approve	2020-11-11 17:28:33.686	2	78
a86ea88e-c9ce-4be0-a0fb-0ff9250c113b	\N	disburse by : FIN001	2020-11-11 17:28:47.148	5	78
\.


--
-- Data for Name: tbl_loan_type; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_loan_type (loan_type_id, dsc, is_active, loan_type) FROM stdin;
1	type a	t	a
2	type b	t	b
\.


--
-- Data for Name: tbl_member; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_member (member_id, fee_share, tgl_input, id_employee, team_member_id, amount_portion, status) FROM stdin;
b6fc3b85-ee22-4e3b-92f5-2d67009dbae7	50	2020-11-10 16:23:37.659	6	14	\N	0
522976c7-b994-4f73-aaae-31a61a915486	15	2020-11-10 16:23:37.684	3	14	\N	0
82b4f72f-42aa-46a0-99bf-f20eb0174632	15	2020-11-10 16:23:37.707	4	14	\N	0
bbe1e850-e043-464e-aeb4-db08f109f13f	10	2020-11-10 16:23:37.731	7	14	\N	0
59464a5d-03b1-4d01-b74f-701ff77b9f24	10	2020-11-10 16:23:37.757	8	14	\N	0
cd105034-ea9d-429d-94de-5bd32972ef18	50	2020-11-11 17:20:30.761	9	15	\N	\N
65a77dc8-d8fa-4e17-8045-1bc2ed9b34a7	15	2020-11-11 17:20:30.789	10	15	\N	\N
0c34e8a0-c71d-461a-ac14-e577c80c5327	15	2020-11-11 17:20:30.811	11	15	\N	\N
80806a75-845a-48f9-86a3-3429587c72ce	10	2020-11-11 17:20:30.832	12	15	\N	\N
c504bb9f-1b4d-4c4b-be99-0c32c6b97cd8	10	2020-11-11 17:20:30.854	13	15	\N	\N
\.


--
-- Data for Name: tbl_outstanding_loan_a; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_outstanding_loan_a (outstanding_a_uu_id, cut_off_date, disburse_id, disburseable_amount, id_employee, total_loan_amount, outstanding_after_disbursement_amount, tax_year, is_active, number_disbursement) FROM stdin;
f595b768-ac17-42f8-8cdf-1827c8f026d4	2020-03-12	1	27110000	13	0	0	2020	t	1
742a2551-5583-4e7e-89b9-988a4d9b2d89	2020-03-12	1	39120000	3	0	0	2020	t	1
3cbab40e-d6fe-4423-b771-4036bbd0598a	2020-03-12	1	39120000	4	0	0	2020	t	1
abd2c62b-362d-4107-b4bf-4d86a5d7f183	2020-03-12	1	27020000	7	0	0	2020	t	1
867875c8-cc6c-40bc-89db-be77dd2ea30c	2020-03-12	1	27020000	8	0	0	2020	t	1
a748fc05-eef7-4df7-9b9f-bc31ffefe480	2020-03-12	1	125220000	9	0	0	2020	t	1
0bb5aeb0-490a-42b6-8ade-08c5c6ebe71e	2020-03-12	1	124495000	6	0	0	2020	t	1
3f704c17-54a1-4177-984a-03c8650849dd	2020-03-12	1	39720000	10	0	0	2020	t	1
28eecd78-9fd0-42c0-a74b-ac981b74c56a	2020-03-12	1	39270000	11	0	0	2020	t	1
d2c67245-139a-4964-a067-417bd2395476	2020-03-12	1	27570000	12	0	0	2020	t	1
\.


--
-- Data for Name: tbl_outstanding_loan_b; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_outstanding_loan_b (outstanding_id, reimburse_id, tahun_input, tgl_input, loan_id, user_id, total_reimburse_amount, case_id, is_active, total_loan_amount, out_standing, outstanding_b_uu_id, id_employee) FROM stdin;
\N	10	20	2020-02-10 16:36:45	\N	\N	10000000	CASE2001	t	70000000	60000000	d43fa58a-f625-4560-96cf-b65f846f97cd	5
\N	11	20	2020-02-10 16:39:10	\N	\N	2000000	CASE2001	t	70000000	68000000	753f9c90-31be-47e1-97f8-4835841cd097	5
\N	12	20	2020-02-11 17:32:04	\N	\N	10000000	CASE2002	t	70000000	60000000	f2c6edf2-f985-422d-a079-c7715f879ca6	5
\N	13	20	2020-02-11 17:33:43	\N	\N	3000000	CASE2002	t	70000000	67000000	2f8adc8d-b9e1-4753-a9ec-c2c2ae21315c	5
\.


--
-- Data for Name: tbl_password_reset_token; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_password_reset_token (id_token, expiry_date, token, id_employee) FROM stdin;
\.


--
-- Data for Name: tbl_period; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_period (id, bulan_disburse, case_id, disburse_id, id_employee, income_tax_paid_on_prior_period, number_disbursement, prev_disbursement, tax_year, employee_id, status) FROM stdin;
6f417cc7-3e51-4d6c-8d1c-864e32ae3e4d	MARET	CASE2002	1	10	0	1	45000000	2020	LAW005	1
b6cc2c8f-094f-4918-9bdc-3a3156147323	MARET	CASE2002	1	11	0	1	45000000	2020	LAW006	1
224d4ffb-3080-416c-9542-fa90eb0cb3c9	MARET	CASE2002	1	12	0	1	30000000	2020	LAW007	1
aad8c028-9603-433d-859f-0d1b96516e9f	MARET	CASE2002	1	13	0	1	30000000	2020	LAW008	1
6df956ac-a9eb-4cf1-8730-55394e184b75	MARET	CASE2001	1	3	0	1	45000000	2020	LAW001	1
99c6bf5b-76dd-44f0-a0af-7b49f20ccf22	MARET	CASE2001	1	4	0	1	45000000	2020	LAW002	1
6af27177-ebb8-4019-b5ef-730cc9b32d35	MARET	CASE2001	1	7	0	1	30000000	2020	LAW003	1
0c35749f-9bde-41ff-9fc4-4430c825062e	MARET	CASE2001	1	8	0	1	30000000	2020	LAW004	1
bb8a7c79-614d-4acb-a33c-6cdd81ac907c	MARET	CASE2002	1	9	0	1	150000000	2020	DMP002	1
f97a388f-179a-4af9-aae8-c1f52ff65a4f	MARET	CASE2001	1	6	0	1	150000000	2020	DMP001	1
\.


--
-- Data for Name: tbl_professional; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_professional (uuid, amount, input_date, perecentage, status, engagement_id, id_employee) FROM stdin;
\.


--
-- Data for Name: tbl_ptkp; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_ptkp (id_tax, ptkp, tax_status, is_active) FROM stdin;
1	54000000	TK0	1
2	58500000	TK1	1
3	63000000	TK2	1
4	67500000	TK3	1
5	58500000	K0	1
6	63000000	K1	1
7	67500000	K2	1
8	72000000	K3	1
\.


--
-- Data for Name: tbl_reimbursement; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_reimbursement (reimburse_id, approved_amount, approved_by, approved_date, expense_date, note, reimburse_amount, reimbursed_by, reimbursed_date, reimbursement_id, signature, status, id_employee, id_loan, is_active, link_document, tgl_input, remarks, mime_type) FROM stdin;
10	10000000	2	2020-01-12 16:36:18	2020-12-31	Test Reimburse Loan B 1	10000000	5	2020-01-12 16:36:44	RMBCS200101	\N	reimburse	6	62	1	/opt/lawfirm/UploadFile/reimbursement/BCS200101/Building+a+REST+API+with+Spring23.pdf	2020-10-10 16:35:25	\N	\N
12	10000000	2	2020-02-10 17:31:44	2020-12-31	Reimbursement case 2	10000000	5	2020-02-10 17:32:04	RMBCS200202	\N	reimburse	9	77	1	/opt/lawfirm/UploadFile/reimbursement/BCS200202/Building+a+REST+API+with+Spring23.pdf	2020-11-11 17:31:20.177	\N	\N
11	2000000	2	2020-02-11 16:38:49	2020-12-31	Note Reimbursement 2	2000000	5	2020-02-11 16:39:10	RMBCS200102	\N	reimburse	6	63	1	/opt/lawfirm/UploadFile/reimbursement/BCS200102/Building+a+REST+API+with+Spring23.pdf	2020-11-10 16:38:13.378	\N	\N
13	3000000	2	2020-02-11 17:33:17	2020-12-31	Reimbursement CASE2002 2	3000000	5	2020-02-11 17:33:43	RMBCS200203	\N	reimburse	9	78	1	/opt/lawfirm/UploadFile/reimbursement/BCS200203/Building+a+REST+API+with+Spring.pdf	2020-11-11 17:32:59.407	\N	\N
\.


--
-- Data for Name: tbl_reimbursement_history; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_reimbursement_history (id, is_active, response, tgl_input, user_id, reimburse_id) FROM stdin;
3a8a5277-5da7-47b7-9b7f-43ebf70fd55d	1	submited by : DMP001	2020-11-10 16:35:25.047	6	10
d9911e04-4310-4cd8-84aa-9e35a3fa6a1d	1	approved by : ADM001	2020-11-10 16:36:18.274	2	10
36a875f3-6260-48e4-bef0-691791808cca	1	reimburse by : FIN001	2020-11-10 16:36:45.092	5	10
7f2b7718-3c6a-4a3a-a234-cfc2b087e41d	1	submited by : DMP001	2020-11-10 16:38:13.386	6	11
a22e046b-455c-4ba9-baaf-4f48a8219d6e	1	approved by : ADM001	2020-11-10 16:38:50.064	2	11
cb0f6a90-efa6-4bcd-b92f-60b74b0b739b	1	reimburse by : FIN001	2020-11-10 16:39:10.635	5	11
5f62c5cc-c35f-402c-b082-5856f314af10	1	submited by : DMP002	2020-11-11 17:31:20.194	9	12
b011341b-03c9-4871-86c1-013ce2f133ca	1	approved by : ADM001	2020-11-11 17:31:44.215	2	12
d2e4cd22-e346-42d1-a52f-6ad949279fb4	1	reimburse by : FIN001	2020-11-11 17:32:04.754	5	12
38b3846b-44e7-42e6-9cb0-ed1d23d54028	1	submited by : DMP002	2020-11-11 17:32:59.416	9	13
9cc41db6-76ec-40bf-83fb-ae335986d393	1	approved by : ADM001	2020-11-11 17:33:17.15	2	13
aad89138-8a44-4fb3-b7ab-7e5138089d26	1	reimburse by : FIN001	2020-11-11 17:33:43.61	5	13
\.


--
-- Data for Name: tbl_tax; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_tax  FROM stdin;
\.


--
-- Data for Name: tbl_team_member; Type: TABLE DATA; Schema: public; Owner: lawfirm
--

COPY public.tbl_team_member (team_member_id, description, dmp_id, fee_share, is_active, tahun_input, engagement_id, amount_portion, status) FROM stdin;
14	TMCS2001	6	50	t	20	18	\N	\N
15	TMCS2002	9	50	t	20	19	\N	\N
\.


--
-- Name: jobid_seq; Type: SEQUENCE SET; Schema: cron; Owner: postgres
--

SELECT pg_catalog.setval('cron.jobid_seq', 1, false);


--
-- Name: runid_seq; Type: SEQUENCE SET; Schema: cron; Owner: postgres
--

SELECT pg_catalog.setval('cron.runid_seq', 1, false);


--
-- Name: account_seq; Type: SEQUENCE SET; Schema: public; Owner: lawfirm
--

SELECT pg_catalog.setval('public.account_seq', 24, true);


--
-- Name: client_seq; Type: SEQUENCE SET; Schema: public; Owner: lawfirm
--

SELECT pg_catalog.setval('public.client_seq', 20, true);


--
-- Name: employee_seq; Type: SEQUENCE SET; Schema: public; Owner: lawfirm
--

SELECT pg_catalog.setval('public.employee_seq', 13, true);


--
-- Name: engagement_seq; Type: SEQUENCE SET; Schema: public; Owner: lawfirm
--

SELECT pg_catalog.setval('public.engagement_seq', 19, true);


--
-- Name: financial_seq; Type: SEQUENCE SET; Schema: public; Owner: lawfirm
--

SELECT pg_catalog.setval('public.financial_seq', 0, true);


--
-- Name: loan_seq; Type: SEQUENCE SET; Schema: public; Owner: lawfirm
--

SELECT pg_catalog.setval('public.loan_seq', 78, true);


--
-- Name: loantype_seq; Type: SEQUENCE SET; Schema: public; Owner: lawfirm
--

SELECT pg_catalog.setval('public.loantype_seq', 2, true);


--
-- Name: ptkp_seq; Type: SEQUENCE SET; Schema: public; Owner: lawfirm
--

SELECT pg_catalog.setval('public.ptkp_seq', 1, false);


--
-- Name: reimburse_seq; Type: SEQUENCE SET; Schema: public; Owner: lawfirm
--

SELECT pg_catalog.setval('public.reimburse_seq', 13, true);


--
-- Name: reset_token_seq; Type: SEQUENCE SET; Schema: public; Owner: lawfirm
--

SELECT pg_catalog.setval('public.reset_token_seq', 1, false);


--
-- Name: role_seq; Type: SEQUENCE SET; Schema: public; Owner: lawfirm
--

SELECT pg_catalog.setval('public.role_seq', 6, true);


--
-- Name: team_member_seq; Type: SEQUENCE SET; Schema: public; Owner: lawfirm
--

SELECT pg_catalog.setval('public.team_member_seq', 15, true);


--
-- Name: tbl_account tbl_account_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_account
    ADD CONSTRAINT tbl_account_pkey PRIMARY KEY (account_id);


--
-- Name: tbl_client_data tbl_client_data_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_client_data
    ADD CONSTRAINT tbl_client_data_pkey PRIMARY KEY (id_client);


--
-- Name: tbl_disburse_loan tbl_disburse_loan_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_disburse_loan
    ADD CONSTRAINT tbl_disburse_loan_pkey PRIMARY KEY (disbursement_uu_id);


--
-- Name: tbl_disbursement tbl_disbursement_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_disbursement
    ADD CONSTRAINT tbl_disbursement_pkey PRIMARY KEY (disbursement_uu_id);


--
-- Name: tbl_document_case tbl_document_case_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_document_case
    ADD CONSTRAINT tbl_document_case_pkey PRIMARY KEY (case_document_id);


--
-- Name: tbl_document_reimburse tbl_document_reimburse_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_document_reimburse
    ADD CONSTRAINT tbl_document_reimburse_pkey PRIMARY KEY (reimburse_document_id);


--
-- Name: tbl_employee tbl_employee_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_employee
    ADD CONSTRAINT tbl_employee_pkey PRIMARY KEY (id_employee);


--
-- Name: tbl_employee_role tbl_employee_role_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_employee_role
    ADD CONSTRAINT tbl_employee_role_pkey PRIMARY KEY (role_id);


--
-- Name: tbl_engagement_history tbl_engagement_history_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_engagement_history
    ADD CONSTRAINT tbl_engagement_history_pkey PRIMARY KEY (id);


--
-- Name: tbl_engagement tbl_engagement_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_engagement
    ADD CONSTRAINT tbl_engagement_pkey PRIMARY KEY (engagement_id);


--
-- Name: tbl_events tbl_events_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_events
    ADD CONSTRAINT tbl_events_pkey PRIMARY KEY (event_id);


--
-- Name: tbl_financial tbl_financial_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_financial
    ADD CONSTRAINT tbl_financial_pkey PRIMARY KEY (financial_id);


--
-- Name: tbl_loan_history tbl_loan_history_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_loan_history
    ADD CONSTRAINT tbl_loan_history_pkey PRIMARY KEY (id);


--
-- Name: tbl_loan tbl_loan_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_loan
    ADD CONSTRAINT tbl_loan_pkey PRIMARY KEY (id);


--
-- Name: tbl_loan_type tbl_loan_type_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_loan_type
    ADD CONSTRAINT tbl_loan_type_pkey PRIMARY KEY (loan_type_id);


--
-- Name: tbl_member tbl_member_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_member
    ADD CONSTRAINT tbl_member_pkey PRIMARY KEY (member_id);


--
-- Name: tbl_outstanding_loan_b tbl_outstanding_loan_b_pk; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_outstanding_loan_b
    ADD CONSTRAINT tbl_outstanding_loan_b_pk PRIMARY KEY (outstanding_b_uu_id);


--
-- Name: tbl_password_reset_token tbl_password_reset_token_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_password_reset_token
    ADD CONSTRAINT tbl_password_reset_token_pkey PRIMARY KEY (id_token);


--
-- Name: tbl_professional tbl_professional_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_professional
    ADD CONSTRAINT tbl_professional_pkey PRIMARY KEY (uuid);


--
-- Name: tbl_reimbursement_history tbl_reimbursement_history_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_reimbursement_history
    ADD CONSTRAINT tbl_reimbursement_history_pkey PRIMARY KEY (id);


--
-- Name: tbl_reimbursement tbl_reimbursement_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_reimbursement
    ADD CONSTRAINT tbl_reimbursement_pkey PRIMARY KEY (reimburse_id);


--
-- Name: tbl_ptkp tbl_tax_pk; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_ptkp
    ADD CONSTRAINT tbl_tax_pk PRIMARY KEY (id_tax);


--
-- Name: tbl_team_member tbl_team_member_pkey; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_team_member
    ADD CONSTRAINT tbl_team_member_pkey PRIMARY KEY (team_member_id);


--
-- Name: tbl_employee uk_630hxl268vljylnk6rkhfjnrr; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_employee
    ADD CONSTRAINT uk_630hxl268vljylnk6rkhfjnrr UNIQUE (nik);


--
-- Name: tbl_employee uk_8hlv7shb3fsxgs9dbq0uaqf1o; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_employee
    ADD CONSTRAINT uk_8hlv7shb3fsxgs9dbq0uaqf1o UNIQUE (employee_id);


--
-- Name: tbl_employee uk_djvywt1mwv68q3uu8iydny89n; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_employee
    ADD CONSTRAINT uk_djvywt1mwv68q3uu8iydny89n UNIQUE (user_name);


--
-- Name: tbl_employee uk_mgxwx0gcqd00m8n2gt0xujijr; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_employee
    ADD CONSTRAINT uk_mgxwx0gcqd00m8n2gt0xujijr UNIQUE (email);


--
-- Name: tbl_loan uk_obk5cvbapwbjuvduai6io1a3q; Type: CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_loan
    ADD CONSTRAINT uk_obk5cvbapwbjuvduai6io1a3q UNIQUE (loan_id);


--
-- Name: tbl_events update_data_event; Type: TRIGGER; Schema: public; Owner: lawfirm
--

CREATE TRIGGER update_data_event AFTER INSERT OR UPDATE ON public.tbl_events FOR EACH ROW EXECUTE PROCEDURE public.update_data_event();


--
-- Name: tbl_events fk2r3w289fbu3u2423ni14qw2pm; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_events
    ADD CONSTRAINT fk2r3w289fbu3u2423ni14qw2pm FOREIGN KEY (engagement_id) REFERENCES public.tbl_engagement(engagement_id);


--
-- Name: tbl_financial fk2x9533l3un0y2smyffqv3r71w; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_financial
    ADD CONSTRAINT fk2x9533l3un0y2smyffqv3r71w FOREIGN KEY (id_employee) REFERENCES public.tbl_employee(id_employee);


--
-- Name: tbl_outstanding_loan_b fk3b1kxd0pxjgfbyknb0dju43jv; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_outstanding_loan_b
    ADD CONSTRAINT fk3b1kxd0pxjgfbyknb0dju43jv FOREIGN KEY (loan_id) REFERENCES public.tbl_loan(id);


--
-- Name: tbl_employee fk4f134633r9my1ung1scx7qstw; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_employee
    ADD CONSTRAINT fk4f134633r9my1ung1scx7qstw FOREIGN KEY (approved_by) REFERENCES public.tbl_employee(id_employee);


--
-- Name: tbl_reimbursement fk4g3feduvl4xf0ipk7euj85q6v; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_reimbursement
    ADD CONSTRAINT fk4g3feduvl4xf0ipk7euj85q6v FOREIGN KEY (id_employee) REFERENCES public.tbl_employee(id_employee);


--
-- Name: tbl_engagement_history fk559i7rh5by1ovkl7y9brqbkpq; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_engagement_history
    ADD CONSTRAINT fk559i7rh5by1ovkl7y9brqbkpq FOREIGN KEY (engagement_id) REFERENCES public.tbl_engagement(engagement_id);


--
-- Name: tbl_password_reset_token fk5euv1l1xrajx9100bm6ux8njp; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_password_reset_token
    ADD CONSTRAINT fk5euv1l1xrajx9100bm6ux8njp FOREIGN KEY (id_employee) REFERENCES public.tbl_employee(id_employee);


--
-- Name: tbl_engagement fk63d7k6xs6c6urvfv7n28yn2n9; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_engagement
    ADD CONSTRAINT fk63d7k6xs6c6urvfv7n28yn2n9 FOREIGN KEY (id_employee) REFERENCES public.tbl_employee(id_employee);


--
-- Name: tbl_loan fk8o991sq0naahw9lpycbrfypb1; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_loan
    ADD CONSTRAINT fk8o991sq0naahw9lpycbrfypb1 FOREIGN KEY (engagement_id) REFERENCES public.tbl_engagement(engagement_id);


--
-- Name: tbl_loan fk912rfxoktr2xwwjcpexcjv581; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_loan
    ADD CONSTRAINT fk912rfxoktr2xwwjcpexcjv581 FOREIGN KEY (loan_type_id) REFERENCES public.tbl_loan_type(loan_type_id);


--
-- Name: tbl_team_member fka7u3dafy6rqtnp9akjae8v2gf; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_team_member
    ADD CONSTRAINT fka7u3dafy6rqtnp9akjae8v2gf FOREIGN KEY (engagement_id) REFERENCES public.tbl_engagement(engagement_id);


--
-- Name: tbl_financial fkbovsnp4bkn8dd8axtufqyckqy; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_financial
    ADD CONSTRAINT fkbovsnp4bkn8dd8axtufqyckqy FOREIGN KEY (loan_id) REFERENCES public.tbl_loan(loan_id);


--
-- Name: tbl_professional fkbv4fkg7cvavmtser4j24rt5ou; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_professional
    ADD CONSTRAINT fkbv4fkg7cvavmtser4j24rt5ou FOREIGN KEY (engagement_id) REFERENCES public.tbl_engagement(engagement_id);


--
-- Name: tbl_member fkescvqvv493hasiwx6627i3wgq; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_member
    ADD CONSTRAINT fkescvqvv493hasiwx6627i3wgq FOREIGN KEY (id_employee) REFERENCES public.tbl_employee(id_employee);


--
-- Name: tbl_engagement fkg1y1nx45shb8iw56vapw6fwiq; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_engagement
    ADD CONSTRAINT fkg1y1nx45shb8iw56vapw6fwiq FOREIGN KEY (id_client) REFERENCES public.tbl_client_data(id_client);


--
-- Name: tbl_reimbursement_history fki2obbwvmv489q2a3opcqe6fn5; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_reimbursement_history
    ADD CONSTRAINT fki2obbwvmv489q2a3opcqe6fn5 FOREIGN KEY (reimburse_id) REFERENCES public.tbl_reimbursement(reimburse_id);


--
-- Name: tbl_disburse_loan fkkkafjn5lw7p8n62kpokn1gqwp; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_disburse_loan
    ADD CONSTRAINT fkkkafjn5lw7p8n62kpokn1gqwp FOREIGN KEY (id_loan) REFERENCES public.tbl_loan(id);


--
-- Name: tbl_account fkkxr0pqx04h6obq36r0ibym0hk; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_account
    ADD CONSTRAINT fkkxr0pqx04h6obq36r0ibym0hk FOREIGN KEY (id_employee) REFERENCES public.tbl_employee(id_employee);


--
-- Name: tbl_reimbursement fkl2uwofudm15cpyovnr6cy6s03; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_reimbursement
    ADD CONSTRAINT fkl2uwofudm15cpyovnr6cy6s03 FOREIGN KEY (id_loan) REFERENCES public.tbl_loan(id);


--
-- Name: tbl_document_reimburse fkpfuh38dopqje4x5w0d7q07t39; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_document_reimburse
    ADD CONSTRAINT fkpfuh38dopqje4x5w0d7q07t39 FOREIGN KEY (reimburse_id) REFERENCES public.tbl_reimbursement(reimburse_id);


--
-- Name: tbl_document_case fkpsjsxk5r1xl6cw2ev75ajk549; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_document_case
    ADD CONSTRAINT fkpsjsxk5r1xl6cw2ev75ajk549 FOREIGN KEY (engagement_id) REFERENCES public.tbl_engagement(engagement_id);


--
-- Name: tbl_professional fkpw0ny3dduyibfyanvo60jem8e; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_professional
    ADD CONSTRAINT fkpw0ny3dduyibfyanvo60jem8e FOREIGN KEY (id_employee) REFERENCES public.tbl_employee(id_employee);


--
-- Name: tbl_loan fkqkl9xell644nutg95tmaeg165; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_loan
    ADD CONSTRAINT fkqkl9xell644nutg95tmaeg165 FOREIGN KEY (id_employee) REFERENCES public.tbl_employee(id_employee);


--
-- Name: tbl_financial fkr2w0to3un0sonel22vrw1bkh3; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_financial
    ADD CONSTRAINT fkr2w0to3un0sonel22vrw1bkh3 FOREIGN KEY (engagement_id) REFERENCES public.tbl_engagement(engagement_id);


--
-- Name: tbl_member fkrbp6x2o39gtalcedluds0d8gl; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_member
    ADD CONSTRAINT fkrbp6x2o39gtalcedluds0d8gl FOREIGN KEY (team_member_id) REFERENCES public.tbl_team_member(team_member_id);


--
-- Name: tbl_loan_history fkrw4qc5sjycnv0lkcb97k15hg9; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_loan_history
    ADD CONSTRAINT fkrw4qc5sjycnv0lkcb97k15hg9 FOREIGN KEY (id_loan) REFERENCES public.tbl_loan(id);


--
-- Name: tbl_disbursement tbl_disbursement_fk; Type: FK CONSTRAINT; Schema: public; Owner: lawfirm
--

ALTER TABLE ONLY public.tbl_disbursement
    ADD CONSTRAINT tbl_disbursement_fk FOREIGN KEY (engagement_id) REFERENCES public.tbl_engagement(engagement_id);


--
-- Name: job cron_job_policy; Type: POLICY; Schema: cron; Owner: postgres
--

CREATE POLICY cron_job_policy ON cron.job USING ((username = (CURRENT_USER)::text));


--
-- Name: job_run_details cron_job_run_details_policy; Type: POLICY; Schema: cron; Owner: postgres
--

CREATE POLICY cron_job_run_details_policy ON cron.job_run_details USING ((username = (CURRENT_USER)::text));


--
-- Name: job; Type: ROW SECURITY; Schema: cron; Owner: postgres
--

ALTER TABLE cron.job ENABLE ROW LEVEL SECURITY;

--
-- Name: job_run_details; Type: ROW SECURITY; Schema: cron; Owner: postgres
--

ALTER TABLE cron.job_run_details ENABLE ROW LEVEL SECURITY;

--
-- PostgreSQL database dump complete
--

