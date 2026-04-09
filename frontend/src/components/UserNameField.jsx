import { forwardRef, useImperativeHandle, useState } from "react";
import { getErrorMessage } from "../utils/errorUtil";
import { isValidUserName, NAME_MAX_LENGTH } from "../utils/validators";
import FormField from "./FormField";

const UserNameField = forwardRef(function UserNameField({ value, onChange, placeholder }, ref){
    const [error, setError] = useState("");

    const validate = () => {
        setError("")
        if (!value) {
              setError(getErrorMessage("E001", "ユーザ名"));
              return false;
        } 
        
        if (value.length > NAME_MAX_LENGTH) {
            setError(getErrorMessage("E003", "ユーザ名", NAME_MAX_LENGTH));
            return false;
        } 
        
        if (!isValidUserName(value)) {
            setError(getErrorMessage("E002", "ユーザ名"));
            return false;
        }
        return true;
    };

    useImperativeHandle(ref, () => ({
        validate,
        setError,
    }));

    const handleChange = (e) => {
        const trimmed = e.target.value.replace(/[\s\u3000]/g, "");
        onChange(trimmed);
    };

    return (
        <FormField
            id="user_name"
            label="ユーザ名"
            maxLength={NAME_MAX_LENGTH}
            value={value}
            onChange={handleChange}
            placeholder={placeholder}
            error={error}
        />
    );
});

export default UserNameField;