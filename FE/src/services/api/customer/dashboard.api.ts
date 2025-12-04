import request from "@/services/request";

const PREFIX = "/customer/dashboard";

export const getDashboardStats = async () => {
  const res = await request({
    url: `${PREFIX}/stats`,
    method: "GET",
  });
  return res.data;
};

export const getFeaturedProducts = async () => {
  const res = await request({
    url: `${PREFIX}/products`,
    method: "GET",
  });
  return res.data;
};

export const getDashboardCategories = async () => {
  const res = await request({
    url: `${PREFIX}/categories`,
    method: "GET",
  });
  return res.data;
};
