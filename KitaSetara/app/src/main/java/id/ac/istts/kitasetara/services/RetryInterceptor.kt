import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryInterceptor(private val maxRetries: Int) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response: Response? = null
        var attempt = 0
        var success = false

        while (attempt < maxRetries && !success) {
            try {
                response = chain.proceed(request)
                success = response.isSuccessful
            } catch (e: IOException) {
                attempt++
                if (attempt >= maxRetries) {
                    throw e
                }
            }
        }

        return response ?: throw IOException("Max retries reached")
    }
}
