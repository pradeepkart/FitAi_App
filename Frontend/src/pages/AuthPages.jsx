import { useState } from "react";
import { Link } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { ErrorMessage } from "../components/UI";
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
