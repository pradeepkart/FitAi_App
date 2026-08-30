export const Card = ({ title, value, sub, children }) => (
  <section className="card">
    <span className="muted">{title}</span>
    {value != null && <strong className="metric">{value}</strong>}
    {sub && <small>{sub}</small>}
    {children}
  </section>
);
function errorText(error) {
  if (!error) return "";

  const responseMessage = error.response?.data?.message;
  if (typeof responseMessage === "string") return responseMessage;
  if (Array.isArray(responseMessage)) return responseMessage.join(", ");

  if (responseMessage && typeof responseMessage === "object") {
    return Object.entries(responseMessage)
      .map(([field, message]) => `${field}: ${String(message)}`)
      .join(", ");
  }

  if (typeof error.message === "string") return error.message;
  if (typeof error === "string") return error;
  return "Something went wrong. Please try again.";
}

export const ErrorMessage = ({ error }) => {
  const message = errorText(error);
  return message ? <div className="error">{message}</div> : null;
};
export const ProgressBar = ({ value, max = 100 }) => (
  <div className="progress">
    <i style={{ width: `${Math.min(100, (value / max) * 100)}%` }} />
  </div>
);
export const LoadingSpinner = () => <div className="loading">Loading…</div>;
