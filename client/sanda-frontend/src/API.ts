import { createContext } from "react";
import StoreModel from "./models/StoreModel"
import RestResponse from "./models/RestResponse";
import ProductModel from "./models/ProductModel";
import Permission from "./models/Permission";
import MemberModel from "./models/MemberModel";
import { registerModel } from "./models/registerModel";
import cartModel from "./models/CartModel";
import { get } from "http";
import ProductCartModel from "./models/ProductCartModel";
import { OrderModel } from "./models/OrderModel"; // Adjust the path as needed
import axios from "axios";
import RoleModel from "./models/RoleModel";
import CreateStoreModel from "./models/CreateStoreModel";
import { NotificationModel } from "./models/NotificationModel";
import { ProductOrderModel } from "./models/ProductOrderModel";
import ProductDataPrice from "./models/ProductDataPrice";
import StoreRequestModel from "./models/StoreRequestModel";
import PolicyDescriptionModel from "./models/PolicyDescriptionModel";
import AddProductModel from "./models/AddProductModel";


const server: string = 'http://127.0.0.1:8080'; 

export const login = async(username: string, password: string) => {
    ///request REST to login...
    const response=(await axios.post(`${server}/api/user/login`,{username: username, password: password},{headers:{
        'Content-Type': 'application/json',
        //Authorization: `Bearer ${jwt_token}`
      }})).data
    return response;
}
export const loginFromGuest = async(username: string, password: string,guestId: number ) => {
    ///request REST to login...
    const response=(await axios.post(`${server}/api/user/loginFromGuest`,{username: username, password: password, guestId:guestId},{headers:{
        'Content-Type': 'application/json',
        //Authorization: `Bearer ${jwt_token}`
      }})).data
    return response;
}
export const loginUsingJwt = async(username: string, jwt: string) => {
    ///request REST to login...
    const response=(await axios.post(`${server}/api/user/loginUsingJwt?username=${username}`,{},{headers:{
        'Content-Type': 'application/json',
        Authorization: `Bearer ${jwt}`
      }})).data
    return response;
}
export const logout = async(username: string) => {
    ///request REST to login...
    const response=(await axios.post(`${server}/api/user/logout`,{field: username},{headers:{
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }})).data
      return Number.parseInt(response.dataJson);
}
export const enterAsGuest = async() => {
    ///request REST to login...
    const response=(await axios.post(`${server}/api/user/enterAsGuest`)).data
    console.log(response);
    return Number.parseInt(response.dataJson);
}
export const registerMember = async(registerModel: registerModel) => {
    ///request REST to login...
    const response = await (await axios.post(
        `${server}/api/user/register`,
        registerModel,
        {
            headers: {
                'Content-Type': 'application/json',
                // Authorization: `Bearer ${jwt_token}` // Uncomment if you have a JWT token
            }
        }
    )).data;
    return response;
}

export const getStoreInfo = async (storeId: string): Promise<RestResponse> => {
    if(localStorage.getItem('token') == null){
        const response = await fetch(
            `${server}/api/stores/getStoreInfo?storeId=${storeId}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                }
            }
        );
        const data: RestResponse = await response.json();
        return data;
    }
    const response = await fetch(
        `${server}/api/stores/getStoreInfo?username=${localStorage.getItem("username")}&storeId=${storeId}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data: RestResponse = await response.json();
    return data;
}

export const getStoreDiscounts = async (storeId: string): Promise<PolicyDescriptionModel[]> => {
    if(localStorage.getItem('token') == null){
        const response = await fetch(
            `${server}/api/stores/describeStoreDiscountPolicy?storeId=${storeId}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                }
            }
        );
        const data: RestResponse = await response.json();
        return JSON.parse(data.dataJson);
    }
    const response = await fetch(
        `${server}/api/stores/describeStoreDiscountPolicy?username=${localStorage.getItem("username")}&storeId=${storeId}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data: RestResponse = await response.json();
    return JSON.parse(data.dataJson);
}

export const getStorePolicies = async (storeId: string): Promise<PolicyDescriptionModel[]> => {
    if(localStorage.getItem('token') == null){
        const response = await fetch(
            `${server}/api/stores/describeStoreBuyPolicy?storeId=${storeId}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                }
            }
        );
        const data: RestResponse = await response.json();
        return JSON.parse(data.dataJson);
    }
    const response = await fetch(
        `${server}/api/stores/describeStoreBuyPolicy?username=${localStorage.getItem("username")}&storeId=${storeId}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data: RestResponse = await response.json();
    return JSON.parse(data.dataJson);
}

export const getStoreProducts = (storeId: string): ProductModel[] => {
    let defaultExample1: ProductModel = {productID:1,productName: "Example1", productPrice: 123.3}
    let defaultExample2: ProductModel = {productID:1,productName: "Example2", productPrice: 3.5}
    let list: ProductModel[] = []
    list.push(defaultExample1)
    list.push(defaultExample2)
    list.push(defaultExample1)
    list.push(defaultExample2)
    list.push(defaultExample1)
    list.push(defaultExample2)
    list.push(defaultExample1)
    list.push(defaultExample2)
    list.push(defaultExample1)
    return list;
}

export const searchAndFilterStoreProducts = (storeId: string, category: string, keywords: string, minprice: number, maxprice: number): ProductModel[] => {
    let defaultExample1: ProductModel = {productID:1,productName: "Example1", productPrice: 123.3}
    let defaultExample2: ProductModel = {productID:1,productName: "Example2", productPrice: 3.5}
    let list: ProductModel[] = []
    if(keywords === "Example1"){
        console.log("AAAAAAAAAAAAAA");
        list.push(defaultExample1)
        list.push(defaultExample1)
        list.push(defaultExample1)
        list.push(defaultExample1)
        list.push(defaultExample1)
    }else if(keywords === "Example2"){
        list.push(defaultExample2)
        list.push(defaultExample2)
        list.push(defaultExample2)
        list.push(defaultExample2)
    }else{
        list.push(defaultExample1)
        list.push(defaultExample2)
        list.push(defaultExample1)
        list.push(defaultExample2)
        list.push(defaultExample1)
        list.push(defaultExample2)
        list.push(defaultExample1)
        list.push(defaultExample2)
        list.push(defaultExample1)
    }
    
    return list;
}

export const isOwner = async (storeId: string): Promise<boolean> => {
    const response = await fetch(
        `${server}/api/stores/isOwner?actorUsername=${localStorage.getItem("username")}&storeId=${storeId}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data: RestResponse = await response.json();
    return (!data.error) && (data.dataJson === "true")
}

export const isFounder = async (storeId: string): Promise<boolean> => {
    const response = await fetch(
        `${server}/api/stores/isFounder?actorUsername=${localStorage.getItem("username")}&storeId=${storeId}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data: RestResponse = await response.json();
    return (!data.error) && (data.dataJson === "true")
}

export const isManager = async(storeId: string): Promise<boolean> => {
    const response = await fetch(
        `${server}/api/stores/isManager?actorUsername=${localStorage.getItem("username")}&storeId=${storeId}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data: RestResponse = await response.json();
    return (!data.error) && (data.dataJson === "true")
}

export const hasPermission = async (storeId: string, permission: Permission): Promise<boolean> => {
    const response = await fetch(
        `${server}/api/stores/hasPermission?actorUsername=${localStorage.getItem("username")}&actionUsername=${localStorage.getItem("username")}&storeId=${storeId}&permission=${permission}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data: RestResponse = await response.json();
    return (!data.error) && (data.dataJson === "true")
}

export const storeActive = (storeId: string): boolean => {
    return true;
}

export const getPermissions = async (storeId: string): Promise<Permission[]> => {
    const response = await fetch(
        `${server}/api/stores/getManagerPermissionsInt?currentOwnerUsername=${localStorage.getItem("username")}&managerUsername=${localStorage.getItem("username")}&storeId=${storeId}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data: RestResponse = await response.json();
    const perms: number[] = JSON.parse(data.dataJson);
    return perms;
}

export const getMangerPermissions = async (storeId: string, managerUsername: string): Promise<Permission[]> => {
    const response = await fetch(
        `${server}/api/stores/getManagerPermissionsInt?currentOwnerUsername=${localStorage.getItem("username")}&managerUsername=${managerUsername}&storeId=${storeId}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data: RestResponse = await response.json();
    console.log(data)
    const perms: number[] = JSON.parse(data.dataJson);
    return perms;
}

export const updateManagerPermissions = async (storeId: string, managerUsername: string, perms: Permission[]): Promise<boolean> => {
    let permissionRequest = {
        managerUsername: managerUsername,
        storeId: parseInt(storeId),
        permission: perms
    }
    const response = await axios.patch(`${server}/api/stores/changeManagerPermission?username=${localStorage.getItem("username")}`, permissionRequest,{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    }
    );
    const data = await response.data;
    if(data.error){
        alert(data.errorString)
        return false;
    }
    return true;
}

export const getStoreManagers = async (storeId: string): Promise<MemberModel[]> => {
    const response = await fetch(
        `${server}/api/stores/getManagers?username=${localStorage.getItem("username")}&storeId=${storeId}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data: RestResponse = await response.json();
    return (JSON.parse(data.dataJson) as MemberModel[]);
}

export const getStoreOwners = async (storeId: string): Promise<MemberModel[]> => {
    const response = await fetch(
        `${server}/api/stores/getOwners?username=${localStorage.getItem("username")}&storeId=${storeId}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data: RestResponse = await response.json();
    return (JSON.parse(data.dataJson) as MemberModel[]);
}

export const acceptRequest = async (requestId: number) => {
    axios.post(`${server}/api/user/acceptRequest?acceptingName=${localStorage.getItem("username")}&requestID=${requestId}`,{},{headers:{
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }})
}

export const rejectRequest = async (requestId: number) => {
    axios.post(`${server}/api/user/rejectRequest?acceptingName=${localStorage.getItem("username")}&requestID=${requestId}`,{},{headers:{
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }})
}

export const okNotification = async (notifId: number) => {
    axios.post(`${server}/api/user/okNotification?username=${localStorage.getItem("username")}&notifID=${notifId}`,{},{headers:{
        'Content-Type': 'application/json',
        Authorization: `Bearer ${localStorage.getItem("token")}`
      }})
}

export const getMember = async(username: string): Promise<MemberModel> => {
      const response = await fetch(
        `${server}/api/user/getUserDTO?username=${username}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data = await response.json();
    
    // Assuming the API returns the data in dataJson
    const profileData = JSON.parse(data.dataJson) as MemberModel;
    console.log(profileData);
    // Validate the structure of profileData before returning

    return profileData;
}

export const viewMemberCart = async(username:string): Promise<string> => {
    const response= await fetch(`${server}/api/user/getCart?username=${username}`,{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    })
    const data= await response.json();
    return data;
}

export const viewGuestCart = async(guestId:number): Promise<string> => {
const response= await fetch(`${server}/api/guest/getCart?guestId=${guestId}`);
const data= await response.json();
return data;
}

export const getProductDetails = async (productId: number): Promise<RestResponse> => {
    if(localStorage.getItem('token') == null){
        const response = await fetch(
            `${server}/api/stores/getProductInfo?productId=${productId}`,
            {
                headers: {
                    'Content-Type': 'application/json',
                }
            }
        );
        const data: RestResponse = await response.json();
        return data;
    }
    const response = await fetch(
        `${server}/api/stores/getProductInfo?username=${localStorage.getItem("username")}&productId=${productId}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data: RestResponse = await response.json();
    return data;
}
export const removeFromCart = (cart: cartModel) => {
    console.log("Cart updated");
}
export const updateFirstName = async(firstName:string) => {
    const response = await (await axios.patch(
        `${server}/api/user/setFirstName?username=${localStorage.getItem("username")}`,
        {field: firstName},
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    )).data;
    return response;
}
export const updateLastName = async (lastName:string) => {
    const response = await (await axios.patch(
        `${server}/api/user/setLastName?username=${localStorage.getItem("username")}`,
        {field: lastName},
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    )).data;
    return response;
}
export const updateEmail = async(email:string) => {
    const response = await (await axios.patch(
        `${server}/api/user/setEmailAddress?username=${localStorage.getItem("username")}`,
        {field: email},
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    )).data;
    return response
}
export const updatePhone = async(phone:string) => {
    const response = await (await axios.patch(
        `${server}/api/user/setPhoneNumber?username=${localStorage.getItem("username")}`,
        {field: phone},
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    )).data;
    return response
}
export const updateBirthday = async(birthday:string) => {
    const response = await (await axios.patch(
        `${server}/api/user/setBirthday?username=${localStorage.getItem("username")}`,
        {field: birthday},
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    )).data;
    return response
}

export const searchProducts = async (
    username: string,
    productName: string,
    productCategory: string,
    minProductPrice: number,
    maxProductPrice: number,
    minProductRank: number
): Promise<ProductModel[]> => {
    const response = (await axios.get(`${server}/api/product/getFilteredProducts`, {
        headers: {
            'Content-Type': 'application/json',
            // Authorization: `Bearer ${jwt_token}`
        },
        params: {
            productName,
            productCategory,
            minProductPrice,
            maxProductPrice,
            minProductRank
        }
    })).data;

    const dataJson = JSON.parse(response.dataJson);

    const products: ProductModel[] = dataJson.map((product: any) => ({
        id: product.productID,
        name: product.productName,
        storeId: product.productID, // Assuming storeId is same as productID
        price: product.productPrice,
        productCategory: product.productCategory,
        productDescription: product.description,
        productRank: product.productRank
    }));
   

    return products;
};


export const getOrders = async (username: string): Promise<OrderModel[]> => {
    try {
        const response = (await axios.get(`${server}/api/getOrderHistory`, {
            headers: {
                'Content-Type': 'application/json',
                // Add Authorization header if required
                // Authorization: `Bearer ${jwt_token}`
            },
            params: {
                username: username
            }
        })).data;

        // Parse the JSON response
        const ordersData: { [key: number]: ProductDataPrice[] } = JSON.parse(response.dataJson);
        const orders: OrderModel[] = Object.entries(ordersData).map(([orderId, products], index) => {
            const total = products.reduce((acc, product) => acc + product.newPrice * product.amount, 0);
            const orderProducts: ProductOrderModel[] = products.map(product => ({
                id: product.id,
                name: product.name,
                quantity: product.amount,
                storeId: product.storeId
            }));
            return {
                id: orderId,
                date: `Date ${index + 1}`, // Adjust this to get the actual date if available
                total: total,
                products: orderProducts
            };
        });

        return orders;
        
    } catch (error) {
        console.error("Failed to fetch orders:", error);
        return [];
    }
};
export const fetchUserStores = async (username: string): Promise<RoleModel[]> => {
    const response = await fetch(`${server}/api/user/getUserRoles?username=${username}`,{
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    });
    const res= await response.json() ; 
    const stores= JSON.parse(res.dataJson) as RoleModel[];
    return stores;
};

export const fetchNotifications = async (): Promise<NotificationModel[]> => {
    const response = await fetch(
        `${server}/api/user/getNotifications?username=${localStorage.getItem("username")}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    const data: RestResponse = await response.json();
    if(data.error){
        return [];
    }
    // Assuming the API returns the data in dataJson
    const notifs = JSON.parse(data.dataJson);
    console.log(data.dataJson)
    return notifs
};

export const createNewStore = async (storeModel: CreateStoreModel,storeFounder :string=localStorage.getItem("username") as string) => {
    storeModel.founderUsername=storeFounder;
    const response = await axios.post(`${server}/api/stores/createStore`, storeModel,{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    }
    );
    return response.data;
};

export const addProduct = async (productModel: AddProductModel,storeId: number) => {
    productModel.storeId = storeId;
    productModel.rank = 3;
    const response = await axios.post(`${server}/api/stores/addProductToStore?username=${localStorage.getItem("username")}`, productModel,{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    }
    );
    return response.data;
};

export const sendManagerRequest = async(username: string, storeId: string): Promise<RestResponse> => {
    let request: StoreRequestModel = {appointer: localStorage.getItem("username")!, appointee: username, storeId: parseInt(storeId)}
    const response = await axios.post(`${server}/api/stores/sendStoreManagerRequest`, request,{
              headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    }
    );
    return response.data;
}

export const addProductToCartMember = async (productId:number,storeId:number, amount:number) => {
    const response = await axios.patch(`${server}/api/user/addProductToCart?username=${localStorage.getItem("username")}`,{storeId: storeId, productId:productId,amount:amount},{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    }
    );
    return response.data;
}

export const sendOwnerRequest = async(username: string, storeId: string): Promise<RestResponse> => {
    let request: StoreRequestModel = {appointer: localStorage.getItem("username")!, appointee: username, storeId: parseInt(storeId)}
    const response = await axios.post(`${server}/api/stores/sendStoreOwnerRequest`, request,{
              headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    }
    );
    return response.data;
}

export const addProductToCartGuest = async (productId:number,storeId:number, amount:number) => {
    const response = await axios.patch(`${server}/api/user/addProductToCart?guestId=${localStorage.getItem("guestId")}`,{storeId: storeId, productId:productId,amount:amount}
    );
    return response.data;
}

export const changeProductAmountInCart = async (productId:number,storeId:number, amount:number) => {
    const response = await axios.patch(`${server}/api/user/changeQuantityCart?username=${localStorage.getItem("username")}`,{storeId: storeId, productId:productId,amount:amount,owner:localStorage.getItem("username") as string},{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    }
    );
    return response.data;
}


export const describeDiscountPolicy = async(policyId: string): Promise<RestResponse> => {
    const response = await fetch(
        `${server}/api/stores/describeDiscountPolicy?username=${localStorage.getItem("username")}&policyId=${policyId}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    return response.json();
}

export const describeBuyPolicy = async(policyId: string): Promise<RestResponse> => {
    const response = await fetch(
        `${server}/api/stores/describeBuyPolicy?username=${localStorage.getItem("username")}&policyId=${policyId}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    return response.json();
}

export const describeCondition = async(policyId: string): Promise<RestResponse> => {
    const response = await fetch(
        `${server}/api/stores/describeDiscountCondition?username=${localStorage.getItem("username")}&condId=${policyId}`,
        {
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
    );
    return response.json();
}

export const createMinAmountCondition = async(minAmount: number, applyOn: string, productId: number, categoryName: string): Promise<string> => {
    if(applyOn === "store"){
        const response = await axios.post(`${server}/api/stores/createMinProductOnStoreCondition?username=${localStorage.getItem("username")}&minAmount=${minAmount}`,{},{ 
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
        );
        let data: RestResponse = await response.data;
        return data.dataJson
    }else if(applyOn === "categ"){
        let request = {
            minAmount: minAmount,
            categoryName: categoryName
        }
        const response = await axios.post(`${server}/api/stores/createMinProductOnCategoryCondition?username=${localStorage.getItem("username")}`,request,{ 
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
            }
        }
        );
        let data: RestResponse = await response.data;
        return data.dataJson
    }
    let request = {
        minAmount: minAmount,
        productId: productId
    }
    const response = await axios.post(`${server}/api/stores/createMinProductCondition?username=${localStorage.getItem("username")}`,request,{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    }
    );
    let data: RestResponse = await response.data;
    return data.dataJson
}

export const createMinBuyCondition = async(minBuy: number): Promise<string> => {
    const response = await axios.post(`${server}/api/stores/createMinBuyCondition?username=${localStorage.getItem("username")}&minBuy=${minBuy}`,{},{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    }
    );
    let data: RestResponse = await response.data;
    return data.dataJson
}

export const createCompositeCondition = async(id1: number, id2: number, logic: string): Promise<string> => {
    let type: string = logic.charAt(0) + logic.substring(1).toLowerCase();
    let request = {
        conditionAID: id1,
        conditionBID: id2
    }
    const response = await axios.post(`${server}/api/stores/create${type}Condition?username=${localStorage.getItem("username")}`,request,{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` // Uncomment if you have a JWT token
        }
    }
    );
    let data: RestResponse = await response.data;
    return data.dataJson
}


export const changeProductAmountInCartGuest = async (productId:number,storeId:number, amount:number) => {
    const response = await axios.patch(`${server}/api/user/guest/changeQuantityCart?guestId=${localStorage.getItem("guestId")}`,{storeId: storeId, productId:productId,amount:amount}
    );
    return response.data;
}

export const removeProductFromCart = async (productId:number,storeId:number) => {
    const response = await axios.patch(`${server}/api/user/removeProductFromCart?username=${localStorage.getItem("username")}`,{storeId: storeId, productId:productId},{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` 
        }
    }
    );
    return response.data;
}
export const removeProductFromCartGuest = async (productId:number,storeId:number) => {
    const response = await axios.patch(`${server}/api/user/guest/removeProductFromCart?guestId=${localStorage.getItem("guestId")}`,{storeId: storeId, productId:productId}
    );
    return response.data;
}
export const checkCart= async (username:string) => {
    const response = await axios.post(`${server}/api/user/checkMemberCart?username=${localStorage.getItem("username")}`,{},{ 
        headers: {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${localStorage.getItem("token")}` 
        }
    }
    );
    return response.data;
}

export const checkCartGuest= async (guestId:number) => {
    const response = await axios.post(`${server}/api/user/checkGuestCart?guestId=${guestId}`,{});
    return response.data;
}
