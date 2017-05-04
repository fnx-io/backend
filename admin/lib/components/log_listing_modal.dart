import 'package:admin/util/rest_listing_factory.dart';
import 'package:angular2/core.dart';
import 'package:fnx_rest/fnx_rest.dart';

@Component(
  selector: 'log-listing-modal',
  templateUrl: 'log_listing_modal.html'
)
class LogListingModal implements OnInit {

  @Input()
  RestClient restClient;

  @Output()
  EventEmitter<bool> close = new EventEmitter<bool>();

  RestListing logListing;

  onClose() {
    close.emit(true);
  }

  @override
  ngOnInit() {
    if (restClient == null) {
      throw "restClient must be provided!";
    }
    logListing = RestListingFactory.withPaging(restClient);
  }

}
