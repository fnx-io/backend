import 'dart:async';
import 'dart:html';
import 'package:admin/rest_listing_factory.dart';
import 'package:angular2/core.dart';
import 'package:fnx_rest/fnx_rest.dart';
import 'package:admin/auth.dart' as auth;

@Component(
  selector: 'log-listing-modal',
  templateUrl: 'log_listing_modal.html'
)
class LogListingModal implements OnInit {

  @Input()
  RestClient restClient;

  @Output()
  EventEmitter<bool> close = new EventEmitter<bool>();

  StreamSubscription<KeyboardEvent> keyDownSubscription;

  RestListing logListing;

  onClose() {
    close.emit(true);
  }

  @override
  ngOnInit() {
    // if (restClient == null) { ... }
    logListing = RestListingFactory.withPaging(restClient);
  }

}
