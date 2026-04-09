import { forwardRef, useImperativeHandle, useState } from "react";
import FormField from "./FormField";
import { isValidEmail, MAIL_MAX_LENGTH } from "../utils/validators";
import { getErrorMessage } from "../utils/errorUtil";

const EmailField = forwardRef(function EmailField({ value, onChange, placeholder }, ref) {
    const [error, setError] = useState("");

    //バリデーション関数
    const validate = () => {
        setError("")
        if (!value) {
            setError(getErrorMessage("E001", "メールアドレス"));
            return false;
        }

        if (!isValidEmail(value)) {
            setError(getErrorMessage("E002", "メールアドレス"));
            return false;
        }

        if (value.length > MAIL_MAX_LENGTH) {
            setError(getErrorMessage("E003", "メールアドレス", MAIL_MAX_LENGTH));
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
            id="email"
            label="メールアドレス"
            maxLength={MAIL_MAX_LENGTH}
            value={value}
            onChange={(e) => onChange(e.target.value)}
            placeholder={placeholder}
            error={error}
        />
    );
});

export default EmailField;