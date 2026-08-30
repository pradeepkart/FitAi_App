export const Card = ({ title, value, sub, children }) => (
  <section className="card">
    <span className="muted">{title}</span>
    {value != null && <strong className="metric">{value}</strong>}
    {sub && <small>{sub}</small>}
    {children}
  </section>
);
export const ErrorMessage = ({ error }) =>
  error ? (
    <div className="error">
      {error.response?.data?.message || error.message}
    </div>
  ) : null;
export const ProgressBar = ({ value, max = 100 }) => (
  <div className="progress">
    <i style={{ width: `${Math.min(100, (value / max) * 100)}%` }} />
  </div>
);
export const LoadingSpinner = () => <div className="loading">Loading…</div>;
