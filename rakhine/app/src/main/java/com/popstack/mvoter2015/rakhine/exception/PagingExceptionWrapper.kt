package com.popstack.mvoter2015.rakhine.exception

data class PagingExceptionWrapper(
  val errorMessage: String,
  val originalException: Throwable
) : Throwable() {

  override val message: String?
    get() = errorMessage

  override val cause: Throwable?
    get() = originalException

}