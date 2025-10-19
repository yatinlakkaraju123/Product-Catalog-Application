import axios, { type AxiosInstance, type AxiosRequestConfig, AxiosError } from "axios";

const API_BASE_URL = "http://localhost:9007";

interface LoginResponse {
  username: string;
  accessToken: string;
  token: string;
  role: "user" | "admin";
}

class ApiService {
  public api: AxiosInstance;

  constructor() {
    this.api = axios.create({
      baseURL: API_BASE_URL,
      headers: { "Content-Type": "application/json" },
      withCredentials: true,
    });

    this.setupInterceptors();
  }

  private setupInterceptors() {
    // Attach access token
    this.api.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem("accessToken");
        if (token) config.headers.Authorization = `Bearer ${token}`;
        return config;
      },
      (error) => Promise.reject(error)
    );

    // Handle token refresh on 401
    this.api.interceptors.response.use(
      (res) => res,
      async (error: AxiosError & { config?: AxiosRequestConfig & { _retry?: boolean } }) => {
        const originalRequest = error.config;
        if (error.response?.status === 401 && originalRequest && !originalRequest._retry) {
          originalRequest._retry = true;

          const refreshToken = localStorage.getItem("refreshToken");
          if (refreshToken) {
            try {
              const response = await this.refreshAccessToken(refreshToken);
              localStorage.setItem("accessToken", response.data.accessToken);
              originalRequest.headers!.Authorization = `Bearer ${response.data.accessToken}`;
              return this.api(originalRequest);
            } catch {
              this.logout();
              window.location.href = "/login";
            }
          }
        }
        return Promise.reject(error);
      }
    );
  }

  private refreshAccessToken(refreshToken: string) {
    return axios.post(`${API_BASE_URL}/auth/v1/refresh`, { refreshToken }, {
      headers: { "Content-Type": "application/json" },
      withCredentials: true,
    });
  }

  public login(username: string, password: string) {
    return this.api.post<LoginResponse>(
      "/auth/v1/login",
      { username, password },
      { headers: { "Content-Type": "application/json" } }
    );
  }

  public logout() {
    const refreshToken = localStorage.getItem("refreshToken");
    if (refreshToken) {
      this.api.post("/auth/v1/logout", { refreshToken });
    }
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
  }

  public getProtectedData() {
    return this.api.get("/protected/data");
  }
}

export default new ApiService();
