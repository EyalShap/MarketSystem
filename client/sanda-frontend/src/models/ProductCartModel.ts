export default interface ProductCartModel {
    id: number,
    storeId: number,
    name: string,
    amount: number
    originalPrice: number
    discountedPrice: number
}