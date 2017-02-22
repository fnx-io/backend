import 'dart:async';
import 'package:angular2/core.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:admin/auth.dart' as auth;

@Component(
  selector: 'rest-listing-monitor',
  templateUrl: 'rest_listing_monitor.html'
)
class RestListingMonitor {

  @Input()
  RestListing listing;

}
