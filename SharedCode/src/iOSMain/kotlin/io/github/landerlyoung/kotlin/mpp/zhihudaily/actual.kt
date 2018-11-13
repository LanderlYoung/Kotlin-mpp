package io.github.landerlyoung.kotlin.mpp.zhihudaily

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.pointed
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import kotlinx.coroutines.runBlocking
import platform.Foundation.NSError
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSURL
import platform.Foundation.NSURLConnection
import platform.Foundation.NSURLResponse
import platform.Foundation.sendSynchronousRequest
import platform.Foundation.setHTTPMethod

@Throws(Throwable::class)
actual suspend fun httpGet(url: String): String {
    //  NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
    //    [request setHTTPMethod:@"GET"];
    //    [request setURL:[NSURL URLWithString:url]];
    //
    //    NSError *error = nil;
    //    NSHTTPURLResponse *responseCode = nil;
    //
    //    NSData *oResponseData = [NSURLConnection sendSynchronousRequest:request returningResponse:&responseCode error:&error];
    //
    //    if([responseCode statusCode] != 200){
    //        NSLog(@"Error getting %@, HTTP status code %i", url, [responseCode statusCode]);
    //        return nil;
    //    }
    //
    //    return [[NSString alloc] initWithData:oResponseData encoding:NSUTF8StringEncoding];

    val request = NSMutableURLRequest()
    request.setHTTPMethod("GET")
    request.setURL(NSURL(string = url))

    val response: CPointer<ObjCObjectVar<NSURLResponse?>>? = null
    val error: CPointer<ObjCObjectVar<NSError?>>? = null

    response?.pointed?.reinterpret<NSURLResponse>()

    val responsData = NSURLConnection.sendSynchronousRequest(
            request, response, error)

    return responsData?.bytes?.reinterpret<ByteVar>()?.toKString() ?: "null"
}

fun nonSuspendHttpGet(url: String): String = runBlocking {
    httpGet(url)
}
