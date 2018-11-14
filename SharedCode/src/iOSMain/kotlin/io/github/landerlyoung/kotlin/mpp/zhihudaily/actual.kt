package io.github.landerlyoung.kotlin.mpp.zhihudaily

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import kotlinx.cinterop.value
import kotlinx.io.IOException
import platform.Foundation.NSError
import platform.Foundation.NSHTTPURLResponse
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSURL
import platform.Foundation.NSURLConnection
import platform.Foundation.NSURLResponse
import platform.Foundation.sendSynchronousRequest
import platform.Foundation.setHTTPMethod

@Throws(IOException::class)
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

    memScoped {
        val responsePtr = alloc<ObjCObjectVar<NSURLResponse?>>()
        val errorPtr = alloc<ObjCObjectVar<NSError?>>()

        val responseData = NSURLConnection.sendSynchronousRequest(
                request, responsePtr.ptr, errorPtr.ptr)

        val response = responsePtr.value
        val error = errorPtr.value
        if (error != null) {
            throw IOException(error.localizedDescription())
        }
        if (response is NSHTTPURLResponse) {
            if (response.statusCode != 200L) {
                throw IOException("invalid statusCode ${response.statusCode}")
            }
        }

        return responseData?.bytes?.reinterpret<ByteVar>()?.toKString() ?: "null"
    }
}

