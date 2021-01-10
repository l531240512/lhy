package com.lhy.pro.po;


import java.io.Serializable;
import lombok.Data;

/**
 *
 * 通过工具自动生成，请勿手工修改。表tb_emp的PO对象<br/>
 * 对应表名：tb_emp
 *
 */
@Data
public class TbEmp implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private String sex;
	private Integer age;
	private String address;
	private String email;
	private String passWord;

}
