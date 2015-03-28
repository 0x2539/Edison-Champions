from decorator import decorator
import json

@decorator
def json_endpoint(f, self, request, *args, **kwargs):
    try:
        params = json.loads(request.body)
    except ValueError:
        params = {}
        for key in request.POST.keys():
            if len(request.POST.getlist(key)) == 1:
                params[key] = request.POST[key]
            else:
                params[key] = request.POST.getlist(key)

    return f(self, request, params, *args, **kwargs)
