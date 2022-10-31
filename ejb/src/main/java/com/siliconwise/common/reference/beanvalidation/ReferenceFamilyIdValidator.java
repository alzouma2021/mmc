package com.siliconwise.common.reference.beanvalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.siliconwise.common.reference.Reference;

/** @ReferenceFamilyId validation class
 * @author sysadmin
 *
 */
public class ReferenceFamilyIdValidator  implements ConstraintValidator<ReferenceFamilyId, Reference> {


    protected String familyId ;

    @Override
    public void initialize(ReferenceFamilyId annotation) {
        this.familyId = annotation.value();
    }

	@Override
	public boolean isValid(Reference reference, ConstraintValidatorContext context) {
		
		if (reference == null) return false ;
		
		if (reference.getFamille() == null) return false ;
		
		return reference.getFamille() != null 
					&& reference.getFamille().getId() != null
					&& reference.getFamille().getId().equals(familyId);
	}

}
