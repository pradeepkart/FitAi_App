import { createContext, useContext, useState } from 'react';
import api from '../services/api';
const AuthContext = createContext();

function readStoredUser() {
  const storedUser = localStorage.getItem('user');
  if (!storedUser) return null;
  try {
    const parsedUser = JSON.parse(storedUser);
    if (!parsedUser || typeof parsedUser !== 'object') throw new Error('Invalid stored user');
    return parsedUser;
  } catch {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    return null;
  }
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(readStoredUser);
  const saveSession = ({ token, user: authenticatedUser }) => {
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(authenticatedUser));
    setUser(authenticatedUser);
  };
  const login = async credentials => saveSession((await api.post('/auth/login', credentials)).data);
  const register = async details => saveSession((await api.post('/auth/register', details)).data);
  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setUser(null);
  };
  return <AuthContext.Provider value={{ user, login, register, logout, isAuthenticated: Boolean(user) }}>{children}</AuthContext.Provider>;
}
export const useAuth = () => useContext(AuthContext);
