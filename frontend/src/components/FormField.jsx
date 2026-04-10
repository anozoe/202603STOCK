import "../styles/LoginRegister.css";

function FormField({ id, label, type = "text", maxLength, value, onChange, error, placeholder }) {
  return (
    <div className="input-field">
      <label htmlFor={id}>{label}</label>
      <input
        id={id}
        type={type}
        maxLength={maxLength}
        value={value}
        onChange={onChange}
        placeholder={placeholder}
      />
      {error && <p className="error-text">{error}</p>}
    </div>
  );
}
 
export default FormField;