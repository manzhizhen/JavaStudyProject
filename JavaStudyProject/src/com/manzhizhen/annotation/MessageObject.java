package com.manzhizhen.annotation;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;

public class MessageObject {
	@Pattern(regexp="0[0-7]|99",message="UserIDType格式不正确")
	private String messageContext;
	
	@Range(min=1,max=100, message="sdfdsf")
	private Long messageSize;

	public String getMessageContext() {
		return messageContext;
	}

	public void setMessageContext(String messageContext) {
		this.messageContext = messageContext;
	}

	public Long getMessageSize() {
		return messageSize;
	}

	public void setMessageSize(Long messageSize) {
		this.messageSize = messageSize;
	}

	
	public static void main(String[] args) {
		MessageObject obj = new MessageObject();
		obj.setMessageSize(new Long(99));
		obj.setMessageContext("yyy");
		
		Validator validator = Validation.buildDefaultValidatorFactory()
				.getValidator();
		
		Set<ConstraintViolation<MessageObject>> constraintViolations = validator
				.validate(obj);// 验证某个对象,，其实也可以只验证其中的某一个属性的

		Iterator<ConstraintViolation<MessageObject>> iter = constraintViolations
				.iterator();
		StringBuffer strBuffer = new StringBuffer();
		while (iter.hasNext()) {
			String message = iter.next().getMessage();
			strBuffer.append(message + System.getProperty("line.separator"));
		}
		System.out.println(strBuffer == null ? null : strBuffer.toString());
		
	}
}
