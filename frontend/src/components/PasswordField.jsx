import React, { forwardRef, useImperativeHandle, useState } from 'react'
import { isValidPassword, PASSWORD_MAX_LENGTH, PASSWORD_MIN_LENGTH } from '../utils/validators';
import { getErrorMessage } from '../utils/errorUtil';
import FormField from './FormField';

const PasswordField = forwardRef(function PasswordField({ value, onChange, placeholder}, ref) {
    const [error, setError] = useState("");
    
    const validate = () => {
        setError("")
        if (!value) {
            setError(getErrorMessage("E001", "パスワード"));
            return false;
        }
    
        if (value.length < PASSWORD_MIN_LENGTH || value.length > PASSWORD_MAX_LENGTH) {
            setError(
            getErrorMessage("E004", "パスワード", PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH)
            );
            return false;
        }
        if (!isValidPassword(value)) {
            setError(getErrorMessage("E002", "パスワード"));
            return false;
        }
    
        return true;
    };

    useImperativeHandle(ref, () => ({
        validate,
        setError,
    }));

    return (
        <FormField
            id="password"
            label="パスワード"
            type='password'
            maxLength={PASSWORD_MAX_LENGTH}
            value={value}
            onChange={(e) => onChange(e.target.value)}
            placeholder={placeholder}
            error={error}
        />
    );
});

export default PasswordField;