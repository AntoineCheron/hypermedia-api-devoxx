package com.github.antoinecheron.hypermedia.noannotation.product;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ProductValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return Product.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "field.required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "field.required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "field.required");

    final Product product = (Product) target;

    if (product.price < 0)
      errors.rejectValue("price", "field.min.value", "Price can not be less than 0");
  }

}
