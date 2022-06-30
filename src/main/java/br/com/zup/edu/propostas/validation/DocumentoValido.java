package br.com.zup.edu.propostas.validation;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {}
)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@CPF
@CNPJ
@ConstraintComposition(CompositionType.OR)
@ReportAsSingleViolation
public @interface DocumentoValido {
    String message() default "deve estar em formato e valor coerente a especificação da Receita Federal Brasileira";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
