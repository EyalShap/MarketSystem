import ProductCartModel from "./ProductCartModel";
export default interface cartModel{
    products: ProductCartModel[],
    totalPrice: number
    discountedPrice: number
}