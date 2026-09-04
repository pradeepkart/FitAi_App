import { useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { ErrorMessage } from "../components/UI";
import api from "../services/api";
function Auth({ registering = false }) {
  const auth = useAuth(),
    [error, setError] = useState(),
    [f, setF] = useState({
      name: "",
      email: "",
      password: "",
      age: 25,
      gender: "MALE",
      height: 170,
      weight: 70,
    });
  const submit = async (e) => {
    e.preventDefault();
    try {
      await (registering
        ? auth.register(f)
        : auth.login({ email: f.email, password: f.password }));
      window.location.assign("/dashboard");
    } catch (x) {
      setError(x);
    }
  };
  return (
    <div className="auth">
      <form className="card form" onSubmit={submit}>
        <h1>{registering ? "Create account" : "Welcome back"}</h1>
        <p className="muted">
          Track progress. Build consistency. Feel stronger.
        </p>
        <ErrorMessage error={error} />
        {registering && (
          <input
            placeholder="Full name"
            required
            onChange={(e) => setF({ ...f, name: e.target.value })}
          />
        )}
        <input
          type="email"
          placeholder="Email"
          required
          onChange={(e) => setF({ ...f, email: e.target.value })}
        />
        <input
          type="password"
          placeholder="Password (8+ characters)"
          minLength="8"
          required
          onChange={(e) => setF({ ...f, password: e.target.value })}
        />
        {registering && (
          <>
            <div className="row">
              <input
                type="number"
                placeholder="Age"
                value={f.age}
                onChange={(e) => setF({ ...f, age: +e.target.value })}
              />
              <select
                aria-label="Gender"
                value={f.gender}
                onChange={(e) => setF({ ...f, gender: e.target.value })}
              >
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
                <option value="OTHER">Other</option>
              </select>
            </div>
            <div className="row">
              <input
                type="number"
                placeholder="Height cm"
                value={f.height}
                onChange={(e) => setF({ ...f, height: +e.target.value })}
              />
              <input
                type="number"
                placeholder="Weight kg"
                value={f.weight}
                onChange={(e) => setF({ ...f, weight: +e.target.value })}
              />
            </div>
          </>
        )}
        <button>{registering ? "Start tracking" : "Sign in"}</button>
        {!registering && (
          <small>
            <Link to="/forgot-password">Forgot password?</Link>
          </small>
        )}
        <small>
          {registering ? "Already a member?" : "New here?"}{" "}
          <Link to={registering ? "/login" : "/register"}>
            {registering ? "Sign in" : "Create account"}
          </Link>
        </small>
      </form>
    </div>
  );
}
export const LoginPage = () => <Auth />;
export const RegisterPage = () => <Auth registering />;

export function ForgotPasswordPage() {
  const [email, setEmail] = useState("");
  const [error, setError] = useState();
  const [message, setMessage] = useState("");

  const submit = async (event) => {
    event.preventDefault();
    setError(undefined);
    setMessage("");
    try {
      const response = await api.post("/auth/forgot-password", { email });
      setMessage(response.data.message);
    } catch (requestError) {
      setError(requestError);
    }
  };

  return (
    <div className="auth">
      <form className="card form" onSubmit={submit}>
        <h1>Forgot password</h1>
        <p className="muted">
          Enter your registered email to receive a reset link.
        </p>
        <ErrorMessage error={error} />
        {message && <div className="success">{message}</div>}
        <input
          type="email"
          placeholder="Email"
          value={email}
          required
          onChange={(event) => setEmail(event.target.value)}
        />
        <button>Send reset link</button>
        <small>
          <Link to="/login">Back to sign in</Link>
        </small>
      </form>
    </div>
  );
}

export function ResetPasswordPage() {
  const [searchParams] = useSearchParams();
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState();
  const [message, setMessage] = useState("");

  const submit = async (event) => {
    event.preventDefault();
    setError(undefined);
    setMessage("");
    if (password !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }
    try {
      const response = await api.post("/auth/reset-password", {
        token: searchParams.get("token") || "",
        password,
      });
      setMessage(response.data.message);
      setPassword("");
      setConfirmPassword("");
    } catch (requestError) {
      setError(requestError);
    }
  };

  return (
    <div className="auth">
      <form className="card form" onSubmit={submit}>
        <h1>Choose a new password</h1>
        <p className="muted">
          Your password must contain at least 8 characters.
        </p>
        <ErrorMessage error={error} />
        {message && <div className="success">{message}</div>}
        <input
          type="password"
          placeholder="New password"
          value={password}
          minLength="8"
          required
          onChange={(event) => setPassword(event.target.value)}
        />
        <input
          type="password"
          placeholder="Confirm new password"
          value={confirmPassword}
          minLength="8"
          required
          onChange={(event) => setConfirmPassword(event.target.value)}
        />
        <button>Update password</button>
        <small>
          <Link to="/login">Back to sign in</Link>
        </small>
      </form>
    </div>
  );
}
