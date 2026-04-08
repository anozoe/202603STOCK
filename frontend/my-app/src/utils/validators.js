export const NAME_MAX_LENGTH = 30;
export const MAIL_MAX_LENGTH = 50;
export const PASSWORD_MIN_LENGTH = 8;
export const PASSWORD_MAX_LENGTH = 16;

export function isValidEmail(value) {
  const regex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
  return regex.test(value);
}

export function isValidPassword(value) {
  const hasLetter = /[A-Za-z]/.test(value);
  const hasNumber = /[0-9]/.test(value);
  const hasSymbol = /[^A-Za-z0-9]/.test(value);
  return [hasLetter, hasNumber, hasSymbol].filter(Boolean).length >= 2;
}

// 全角入力を許可するバリデーション
export function isValidUserName(value) {
  const regex = /^[\u3040-\u309F\u30A0-\u30FF\u4E00-\u9FFF\uFF01-\uFF60]+$/;
  return regex.test(value);
}